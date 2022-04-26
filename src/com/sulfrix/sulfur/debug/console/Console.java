package com.sulfrix.sulfur.debug.console;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import processing.core.PGraphics;

import processing.event.KeyEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {

    public int drawLineCount = 30;
    public int drawFontSize = 10;

    static SulfurGame game;

    public static HashMap<String, ConVar> convars = new HashMap<>();
    public static HashMap<String, ConCommand> concommands = new HashMap<>();

    public ArrayList<String> lines;
    public StringBuilder inputbuffer = new StringBuilder();
    public int caret;
    public ArrayList<String> commandHistory = new ArrayList<>();
    public int historyIndex;

    // wow that looks ugly in here
    public static Pattern commandPattern = Pattern.compile("(((?<!\\\\)\"(.*?)(?<!\\\\)\")|(\\S+))");

    public Console(SulfurGame game) {
        lines = new ArrayList<>();
        var consoleout = new ConsoleOut(System.out, this);
        System.setOut(consoleout);
        System.setErr(consoleout);
        Console.game = game;
    }

    public void inputKey(KeyEvent event) {
        char toInput = event.getKey();
        final int[] allowedTypes = new int[]{1, 2, 9, 12, 20, 21, 22, 23, 24, 25, 26, 27};
        //System.out.println("(keyCode) " + event.getKeyCode() + " " + Character.getType(event.getKey()));

        if (event.getKeyCode() == 10) {
            var runcmd = clearInput();
            runCommand(runcmd, true);
            if (runcmd.length() > 0) {
                if (commandHistory.size() == 0 || !runcmd.equals(commandHistory.get(commandHistory.size()-1))) {
                    commandHistory.add(runcmd);
                    historyIndex++;
                }
            }
        }

        if (event.getKeyCode() == 8 && caret > 0) {
            inputbuffer.deleteCharAt(caret - 1);
            caret--;
            if (inputbuffer.length() == 0) {
                historyIndex = commandHistory.size();
            }
            return;
        }
        if (!event.isControlDown() && contains(allowedTypes, Character.getType(toInput))) {
            inputbuffer.insert(caret, toInput);
            caret++;
        }
        if (event.isControlDown()) {
            if (event.getKeyCode() == 68) {
                clearInput();
            }
        }
        switch (event.getKeyCode()) {
            case 37 -> setCaret(caret - 1);
            case 38 -> recallHistory(-1);
            case 39 -> setCaret(caret + 1);
            case 40 -> recallHistory(1);
            default -> {
            }
        }
    }

    public void setCaret(int index) {
        if (index < 0) {
            index = 0;
        }
        if (index > inputbuffer.length()) {
            index = inputbuffer.length();
        }
        caret = index;
    }

    public void recallHistory(int offset) {
        if (commandHistory.size() <= 0) {
            return;
        }
        historyIndex += offset;
        if (historyIndex < 0) {
            historyIndex = 0;
        }
        if (historyIndex >= commandHistory.size()) {
            historyIndex = commandHistory.size() - 1;
        }
        var hist = commandHistory.get(historyIndex);
        inputbuffer = new StringBuilder(hist);
        caret = inputbuffer.length();
    }

    public String clearInput() {
        var out = inputbuffer.toString();
        caret = 0;
        historyIndex = commandHistory.size();
        inputbuffer = new StringBuilder();
        return out;
    }

    public static boolean runCommand(String command, boolean isUser) {
        // parse whole command with /((".*?")|(\S+))/g
        // parse command name with /[a-z_]+/
        if (isUser) {
            System.out.println("] " + command);
        }
        Matcher matcher = commandPattern.matcher(command);
        String commandName;
        if (matcher.find()) {
            commandName = matcher.group().toLowerCase();
            String[] args = parseArgs(matcher);
            if (convars.containsKey(commandName)) {
                ConVar convar = convars.get(commandName);
                if (args.length > 0) {
                    if (convar.userMutable || !isUser) {
                        convar.setValue(args[0]);
                    } else {
                        System.out.println(convar.name + " cannot be modified in the console");
                    }
                } else {
                    System.out.println(convar.name + " = " + convar.getString() + " (" + convar.preferredType + ", default " + convar.defaultValue + ")");
                    System.out.println(convar.desc);
                }
            } else if (concommands.containsKey(commandName)) {
                ConCommand conCommand = concommands.get(commandName);
                conCommand.exec(game, args);
            } else {
                System.out.println("Unknown command/convar " + commandName);
            }
        }
        return true;
    }

    public static void writeToFile(String name, int forceLevel) throws IOException {
        if (!name.endsWith(".cfg")) {
            name+= ".cfg";
        }
        FileWriter fileWriter = new FileWriter("cfg/" + name);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (String k : convars.keySet()) {
            ConVar var = convars.get(k);
            if (var.saveValue || forceLevel > 1) {
                if (!var.getString().equals(var.defaultValue) || forceLevel > 0) {
                    printWriter.println(var.writeCommand());
                }
            }
        }
        printWriter.close();
    }

    public static void loadFromFile(String name) throws IOException {
        if (!name.endsWith(".cfg")) {
            name+= ".cfg";
        }
        Scanner scanner = new Scanner(new File("cfg/" + name));
        while (scanner.hasNext()) {
            Console.runCommand(scanner.nextLine());
        }
    }

    public static void resetAll() {
        for (ConVar var : convars.values()) {
            var.setValue(var.defaultValue);
        }
    }

    public static String[] parseArgs(Matcher matcher) {
        //System.out.println("Running " + commandName);
        ArrayList<String> args = new ArrayList<>();
        while (matcher.find()) {
            var arg = matcher.group();
            if (matcher.group(3) != null) {
                arg = matcher.group(3);
            }
            //System.out.println(">" + arg);
            args.add(arg);
        }
        return args.toArray(new String[0]);
    }

    public static String[] parseArgs(String string) {
        var matcher = commandPattern.matcher(string);
        return parseArgs(matcher);
    }

    public static boolean runCommand(String command) {
        return runCommand(command, false);
    }

    public static void addConVar(ConVar convar) {
        convars.put(convar.name, convar);
    }

    public static ConVar getConVar(String name) {
        return convars.get(name);
    }

    public static void addConCommand(ConCommand conCommand) {
        concommands.put(conCommand.name, conCommand);
    }

    // This is stupid.
    public static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    public void draw(PGraphics g) {
        drawFontSize = Math.max(10, Console.getConVar("console_fontsize").getInt());
        g.push();
        FontManager.quickUse(g, "Arial", drawFontSize);
        g.textSize(drawFontSize);
        drawLineCount = (g.height / drawFontSize) - 4;
        var rectHeight = ((drawLineCount + 1) * drawFontSize) + g.textDescent()*2 + 3;
        g.translate(0, 0, 20);
        g.noStroke();
        g.fill(0, 127);
        g.rect(0, 0, g.width, rectHeight);
        g.fill(0, 255, 0);
        {
            g.push();
            for (int i = 0; i < drawLineCount; i++) {
                int x = lines.size() - i - 1;
                if (x >= lines.size() || x < 0) {
                    break;
                }
                g.push();
                g.translate(0, (drawLineCount - i) * drawFontSize);
                g.text(lines.get(x), 0, 0);
                g.pop();
            }
            g.pop();
        }
        float inputPos = (drawLineCount * drawFontSize) + g.textDescent();
        {
            g.push();
            g.translate(0, inputPos);
            g.rect(0, 0, g.width, 1);
            g.pop();
        }
        {
            g.push();
            g.translate(0, inputPos+drawFontSize+2);
            var caretPos = g.textWidth(inputbuffer.substring(0, caret));
            if (caretPos > g.width - 50) {
                g.push();
                g.fill(0, 255, 0, 127);
                g.rect(0, -drawFontSize, 20, drawFontSize + g.textDescent());
                g.pop();
                g.translate(-(caretPos - (g.width - 50)), 0);
            }
            g.text(inputbuffer.toString(), 0, 0);
            g.rect(caretPos, -drawFontSize, 1, drawFontSize + g.textDescent());
            g.pop();
        }
        g.rect(0, rectHeight, g.width, 1);
        g.pop();

    }

    public static class ConsoleOut extends PrintStream {
        public String buffer = "";
        public Console owner;

        public ConsoleOut(OutputStream out, Console owner) {
            super(out);
            this.owner = owner;
        }

        public void scanChar(char c) {
            if (c != '\n') {
                buffer += c;
            } else {
                owner.lines.add(buffer);
                buffer = "";
            }
        }

        @Override
        public void print(char c) {
            scanChar(c);
            super.print(c);
        }

        @Override
        public void print(String s) {
            for (char c : s.toCharArray()) {
                scanChar(c);
            }
            super.print(s);
        }

        @Override
        public void println(String x) {
            print(x);
            print("\n");
        }

        @Override
        public void println(char x) {
            println(Character.toString(x));
        }
    }
}

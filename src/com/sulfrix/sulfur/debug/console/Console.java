package com.sulfrix.sulfur.debug.console;

import com.sulfrix.sulfur.lib.GlobalManagers.FontManager;
import processing.core.PGraphics;

import processing.event.KeyEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {

    public int drawLineCount = 30;
    public int drawFontSize = 10;

    protected static HashMap<String, ConVar> convars = new HashMap<>();
    protected static HashMap<String, ConCommand> concommands = new HashMap<>();

    public ArrayList<String> lines;
    public StringBuilder inputbuffer = new StringBuilder();
    public int caret;
    public ArrayList<String> commandHistory = new ArrayList<>();
    public int historyIndex;

    public Console() {
        lines = new ArrayList<>();
        var consoleout = new ConsoleOut(System.out, this);
        System.setOut(consoleout);
        System.setErr(consoleout);
    }

    public void inputKey(KeyEvent event) {
        char toInput = event.getKey();
        final int[] allowedTypes = new int[] {1, 2, 9, 12, 20, 21, 22, 23, 24, 25, 26, 27};
        //System.out.println("(keyCode) " + event.getKeyCode() + " " + Character.getType(event.getKey()));

        if (event.getKeyCode() == 10) {
            var runcmd = clearInput();
            runCommand(runcmd, true);
            if (runcmd.length() > 0) {
                commandHistory.add(runcmd);
                historyIndex++;
            }
        }

        if (event.getKeyCode() == 8 && caret > 0) {
            inputbuffer.deleteCharAt(caret-1);
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
            historyIndex = commandHistory.size()-1;
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
        Pattern commandPattern = Pattern.compile("((\"(.*?)\")|(\\S+))");
        Matcher matcher = commandPattern.matcher(command);
        String commandName = "";
        ArrayList<String> args = new ArrayList<>();
        if (matcher.find()) {
            commandName = matcher.group().toLowerCase();
            //System.out.println("Running " + commandName);
            while (matcher.find()) {
                var arg = matcher.group();
                if (matcher.group(3) != null) {
                    arg = matcher.group(3);
                }
                //System.out.println(">" + arg);
                args.add(arg);
            }
            if (convars.containsKey(commandName)) {
                ConVar convar = convars.get(commandName);
                if (args.size() > 0) {
                    if (convar.userMutable || !isUser) {
                        convar.setValue(args.get(0));
                    } else {
                        System.out.println(convar.name + " cannot be modified in the console");
                    }
                } else {
                    System.out.println(convar.name + " = " + convar.getString());
                }
            } else if (concommands.containsKey(commandName)) {

            } else {
                System.out.println("Unknown command/convar " + commandName);
            }
        }

        return true;
    }

    public static void addConVar(ConVar convar) {
        convars.put(convar.name, convar);
    }

    public static ConVar getConVar(String name) {
        return convars.get(name);
    }

    // This is stupid.
    public static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    public void draw(PGraphics g) {
        drawLineCount = (g.height/drawFontSize)-4;
        var rectHeight = ((drawLineCount+1)*drawFontSize)+8;
        {
            g.push();
            g.translate(0, 0, 20);
            g.noStroke();
            g.fill(0, 127);
            g.rect(0, 0, g.width, rectHeight);
            FontManager.quickUse(g, "Arial", drawFontSize);
            g.textSize(drawFontSize);
            g.fill(0, 255, 0);
            {
                g.push();
                for (int i = 0; i < drawLineCount; i++) {
                    int x = lines.size()-i-1;
                    if (x >= lines.size() || x < 0) {
                        break;
                    }
                    g.push();
                    g.translate(0, (drawLineCount-i)*drawFontSize);
                    g.text(lines.get(x), 0, 0);
                    g.pop();
                }
                g.pop();
            }
            {
                g.push();
                g.translate(0, (drawLineCount*drawFontSize)+2);
                g.rect(0, 0, g.width, 1);
                g.pop();
            }
            {
                g.push();
                g.translate(0, ((drawLineCount+1)*drawFontSize)+4);
                var caretPos = g.textWidth(inputbuffer.substring(0, caret));
                if (caretPos > g.width-50) {
                    g.push();
                    g.fill(0, 255, 0, 127);
                    g.rect(0, -drawFontSize, 20, drawFontSize+g.textDescent());
                    g.pop();
                    g.translate(-(caretPos-(g.width-50)), 0);
                }
                g.text(inputbuffer.toString(), 0, 0);
                g.rect(caretPos, -drawFontSize, 1, drawFontSize+g.textDescent());
                g.pop();
            }
            g.rect(0, rectHeight, g.width, 1);
            g.pop();
        }
    }

    public class ConsoleOut extends PrintStream {
        public String buffer = "";
        public Console owner;

        public ConsoleOut(OutputStream out, Console owner) {
            super(out);
            this.owner = owner;
        }

        public void scanChar(char c) {
            if (c != '\n') {
                buffer+=c;
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
            println(x);
        }
    }
}

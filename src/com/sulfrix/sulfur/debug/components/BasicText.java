package com.sulfrix.sulfur.debug.components;

import com.sulfrix.sulfur.SulfurGame;
import com.sulfrix.sulfur.debug.InfoComponent;
import processing.core.PGraphics;

public class BasicText extends InfoComponent {

    BasicTextLambda textLambda;

    public BasicText(BasicTextLambda lambda, boolean expandedOnly) {
        super(expandedOnly);
        textLambda = lambda;
    }

    @Override
    public float draw(PGraphics g, SulfurGame game) {
        return basicDrawText(g, textLambda.eval(game));
    }

    public interface BasicTextLambda {
        String eval(SulfurGame game);
    }
}

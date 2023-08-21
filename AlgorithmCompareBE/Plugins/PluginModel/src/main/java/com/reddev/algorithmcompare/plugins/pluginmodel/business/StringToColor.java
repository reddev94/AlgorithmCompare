package com.reddev.algorithmcompare.plugins.pluginmodel.business;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum StringToColor {
    RED("#FF0000"),
    YELLOW("#FFFF00"),
    GREEN("#008000"),
    MAGENTA("#FF00FF"),
    BLUE("#36b5d8");

    private final String value;

    //****** Reverse Lookup Implementation************//
    //Lookup table
    private static final Map<String, StringToColor> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(StringToColor color : StringToColor.values())
        {
            lookup.put(color.getValue(), color);
        }
    }

    //This method can be used for reverse lookup purpose
    public static StringToColor get(String value)
    {
        return lookup.get(value);
    }
}

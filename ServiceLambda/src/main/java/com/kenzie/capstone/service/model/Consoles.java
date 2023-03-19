package com.kenzie.capstone.service.model;

public enum Consoles {
    X360("Xbox 360"),
    PC("Personal Computer"),
    PS3("PlayStation 3"),
    XONE("Xbox One"),
    PS4("PlayStation 4"),
    NS("Nintendo Switch"),
    XBSX("Xbox Series X"),
    PS5("PlayStation 5"),
    VITA("PlayStation Vita"),
    WIIU("Wii U"),
    STA("Stadia"),
    MAC("Macintosh"),
    DS("Nintendo DS"),
    GC("GameCube"),
    GBA("Game Boy Advance"),
    WII("Wii"),
    PS2("PlayStation 2"),
    AND("Android"),
    IOS("iOS");

    private final String name;

    private Consoles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

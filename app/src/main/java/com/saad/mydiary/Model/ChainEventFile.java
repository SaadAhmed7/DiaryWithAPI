package com.saad.mydiary.Model;

public class ChainEventFile {
    String Titlel;
    int ChainID;

    public ChainEventFile() {
    }

    public ChainEventFile(String titlel, int chainID) {
        Titlel = titlel;
        ChainID = chainID;
    }

    public String getTitlel() {
        return Titlel;
    }

    public void setTitlel(String titlel) {
        Titlel = titlel;
    }

    public int getChainID() {
        return ChainID;
    }

    public void setChainID(int chainID) {
        ChainID = chainID;
    }
}

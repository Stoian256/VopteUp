package com.java.voteup.domain;

import com.java.voteup.utils.Utils;

import java.util.Date;

public class Block {
    private String hash;
    private String previousHash;
    private Vote voteData;
    private long timeStamp;
    private int nonce;

    public Block(Vote voteData, String previousHash) {
        this.voteData = voteData;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return Utils.applySha256(
                previousHash +
                        timeStamp +
                        nonce +
                        voteData
        );

    }

    public void mineBlock(int difficulty) {
        String target = Utils.getDifficultyString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public Vote getVoteData() {
        return voteData;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setVoteData(Vote voteData) {
        this.voteData = voteData;
    }
}

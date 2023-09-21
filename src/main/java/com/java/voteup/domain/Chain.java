package com.java.voteup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chain {
    private ArrayList<Block> chain;
    private int difficulty;

    public Chain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;
    }

    public void addBlock(Block block) {
        block.mineBlock(difficulty);
        chain.add(block);
    }

    public Block getLatestBlock() {
        if (chain.size() == 0)
            return new Block(null, "0");
        return chain.get(chain.size() - 1);
    }

    public boolean isChainValid(List<Vote> votes, Integer eventId) {
        for (int i = 0; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Vote chainVote = currentBlock.getVoteData();
            if (Objects.equals(chainVote.getEventId(), eventId)) {
                Vote realVote = votes.stream().filter(t -> Objects.equals(t.getUserId(), chainVote.getUserId())).findFirst().get();
                Block realBlock = currentBlock;
                realBlock.setVoteData(realVote);
                if (!currentBlock.getHash().equals(realBlock.calculateHash())) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Block> getAllBlocks() {
        return chain;
    }
}
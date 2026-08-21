package com.externalapicalling.payload;

public class GeminiResponse {
    private Candidates[] candidates;

    public GeminiResponse() {
    }

    public Candidates[] getCandidates() {
        return candidates;
    }

    public void setCandidates(Candidates[] candidates) {
        this.candidates = candidates;
    }
}

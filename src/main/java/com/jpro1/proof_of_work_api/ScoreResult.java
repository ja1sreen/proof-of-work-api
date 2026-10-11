package com.jpro1.proof_of_work_api;

public record ScoreResult(String username, double score, Breakdown breakdown) {

    public record Breakdown(
            double activity,
            double originalWork,
            double projectQuality,
            double collaboration,
            double languageDiversity,
            double communitySignal) {}
}
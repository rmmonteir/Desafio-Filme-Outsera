package com.golden.movie_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwardIntervalResponse {
    private List<AwardInterval> min;
    private List<AwardInterval> max;
}
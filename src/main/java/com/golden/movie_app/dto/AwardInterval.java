package com.golden.movie_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwardInterval {
    private String producer;
    private int interval;
    private int previousWin;
    private int followingWin;
}
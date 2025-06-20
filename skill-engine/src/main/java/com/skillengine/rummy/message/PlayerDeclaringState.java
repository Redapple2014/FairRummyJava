package com.skillengine.rummy.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDeclaringState
{
	private long playingPlayer;
	private List< List< String > > groupCards;
	private int points;
	private int status;

}

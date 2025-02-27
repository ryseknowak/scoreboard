# Live Score Board

Interview task for Sportradar

## Description

This library implements basic functionality for live tracking matches and their scores.
It was limited to most simple solution possible - no DB, no views, just core logic.

To use its functionality refer
to [LiveScoreBoard](src/main/java/com/sportradar/scoreboard/core/service/LiveScoreBoard.java)
public interface.

## Comments on task

I had to make couple assumptions to make functional description of the task more precise:

- The summary does not include information about finished games, therefore no information about finished games is
  persisted.
- Match "total score" is just a sum of home and away score.
- Solution do not accept nulls and empty Strings as Home or Away teams
- As solution is meant for Football World Cup it should accept only positive scores
- Same team cannot participate in 2 matches at once

### Technical limitations

- Score is implemented as an integer - therefore we limit possible future usage to sports which have not fractional
  scores limited by 2^16
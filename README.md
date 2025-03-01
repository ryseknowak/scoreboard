# Live Score Board

Interview task for Sportradar

## Description

This library implements basic functionality for live tracking matches and their scores.
It was limited to most simple solution possible - no DB, no views, just core logic.

To use its functionality refer
to [LiveScoreBoard](src/main/java/com/sportradar/scoreboard/core/service/LiveScoreBoard.java)
public interface.

## Comments on task

During the implementation of this task, the most challenging aspect for me was finding the right balance between
delivering the "simplest possible functional implementation" and ensuring a high-quality, well-structured solution.
Instead of opting for the most straightforward functional approach, I focused on designing a solution that follows
core design principles and best practices.

My key design choices were driven by:

- Extensibility – ensuring that the MVP could evolve without major refactoring.
- Separation of Concerns – keeping different responsibilities clearly defined.
- Code Maintainability & Readability – making the solution easy to understand.

I believe that while simplicity is important, providing some more code to maintain a higher standard of software design
leads to more scalable, maintainable, and adaptable solutions in the long run. Nevertheless, I tried not to overengineer
things and recall "good enough" rule when applicable. Would love to discuss your feedback on my solution in details.

I had to make couple assumptions to make functional description of the task more precise:

- The summary does not include information about finished games, therefore no information about finished games is
  persisted.
- Match "total score" is just a sum of home and away score.
- Solution do not accept nulls and empty Strings as Home or Away teams
- As solution is meant for Football World Cup it should accept only positive scores
- Same team cannot participate in 2 matches at once
- Match cannot take place between same teams
- Score can be lowered (e.g. when a goal was scored but disallowed by VAR or any other mistake was done)

### Technical decisions / limitations

- Score is implemented as an integer - therefore we limit possible future usage to sports which have not fractional
  scores limited by 2^16
- Start timestamp is implemented as long instead of Instant to fulfill the only current requirement (which was sorting
  by the order of entity creation) and keep things easier to implement.
# Risk
SYSC 3110 project of the game risk

-------------------------------------------------------------------------------------------------------
README
-------------------------------------------------------------------------------------------------------

Authors:
- Daniel Wall
  - Attack and Movement controllers
  - Game model
  - JSON saving/loading
- Khalid Kana’an
  - (Dropping the course)
- Rahel Gunaratne
- Francois Argent

Deliverables:
1. Current iteration
2. GUI iteration: Add a gui using a JFrame and mouse interaction
3. Additional Features iteration: add placing armies, moving armies, and ai player
4. Save iteration: Add saving/loading, and 

Changes/Additions:
- Added GUI, both the map and info panel
- Added loading of map from JSON
- Made the game follow MVC pattern (so added view and game model)
- Added a movement controller to separate functionality from main controller
- Created map in json using the incomplete map editor (can be tested/used)

Roadmap:
- Add ai player
- Make moving armies more complete
- Add more polished map editing

Issues:
- Can move armies from a country to the same country, there should be a check if the player selected the same country.

# INTERACTIVE MicroTales

Hugo Zaragoza (hugo@hugo-zaragoza.net), May 2015.

>*for Leo and Nat, my own wonderful microtale...*

## Concept ##

>User says: "Once upon a time there was a princess and a dragon". 

>Screen: a beautiful drawing unfolds in the screen, of a beautiful Princess and a scary Dragon. Suddently they come to life and start breathing and looking around. They have not noticed each other yet, so they wonder aimelessly...

>Screen: the Dragon notices the Princess and flies agressively towards her! She notices the Dragon and scares, running in the opposite direction. But the Dragon is much faster and will surely catch her...

>User says: "the princess has a crossbow!"

>Screen: with a flash, a crossbow appears in the hands of the Princess. She smiles and turns around to attack the Dragon! 

An *Interactive MicroTale* (IMIT) is a game played cooperatively by a computer and one or more humans. The computer runs a simulation of a world continuously, showing in beautiful graphics the activities of the different characters. The different chatacters in the game evolve following their pre-programmed behaviours: they run when scared, look for food when hungry, fall in love, etc.

A *Language Interactive Microtale* (LIMIT) is an IMIT in which the only interface between the game and the player is a microphone. The human player can only interact with the unfolding game by saying certain things to the game. Using speech, the player can introduce into the story new characters, new objetcs, new behaviours, etc. But once they are introduced into the game world, they will follow their own behaviours. **Users "tell the story" and watch it unfold.**

### variants:

Many different types of IMITs are possible. For example:

* open ended: the story evolves, without any specific objective in mind
* goal driven: to reach the goal certain characters need to acomplish certain acts
* multiplayer: different people interact with the story. maybe some characters only listen to some players... maybe each player has different objectives... we can set up cooprative games where only by cooperation can players win.

Also interfaces can be of multiple mixed types:

* user may be a player with traditional interface controls (joystick, etc.) while carrying some actions by speech.

* audio only: the computer "explains back" to the player what is happening, without an actual screen (only audio in and audio out, without a screen!)


## Implementation Overview

The game dynamics are as follows:

  * Game world (GW) evolves in real time following its dynamics
  * WHEN player utters sentence:
    * Sentence is parsed and converted into changes in the game world

Java packages are splitted as follows:

  * grammar : langauge world code
  * gameworld : game world code
  * imit : puts all together into an application (game)

### Documentation

* [TODO](todo.md)
* [IMIT4J Intro](imit4j.md)
* [Linguistic Parsing](lingParsing.md)
* [Game World](gameWorld.md)



### Implementation Bibliography

**Feature Structures**

  * Definition: <http://en.wikipedia.org/wiki/Feature_structure>
  * XML schema for complex FS, Feature-Value libraries, Feature-Value expressions...  <http://www.tei-c.org/release/doc/tei-p5-doc/en/html/FS.html>
  * PC-PATR <http://www-01.sil.org/pcpatr/manual/pcpatr.html>


**Typed Feature Structures**

  * I could not find a decent Java library for Feature Structures, I implemented my own preliminary embryo with what I needed in  `net.hugo-zaragoza.ballester.datastructures.fs`

* Copestake: <http://web.stanford.edu/group/cslipublications/cslipublications/pdf/1575862603h.pdf>
* GLARF: <http://nlp.cs.nyu.edu/meyers/GLARF.html>
* SPROUT: <http://www.powershow.com/view/1b752-MmI1Y/SProUT_Shallow_Processing_with_Unification_and_Typed_Feature_Structures_powerpoint_ppt_presentation> and <http://www.medlingmap.org/node/98>

**Semantics**

  * Minimal Recursion Semantics: An Introduction. ANN COPESTAKE1, DAN FLICKINGER2, CARL POLLARD3 and IVAN A. SAG4

**Game Libraries**

  * https://www.lwjgl.org/

##TODO

###Open Issues

REVIEW SemanticParser.parsem1 and SemanticParser.parsem2 with respecto to theory in [Linguistic Parsing](lingParsing.md)


"The green princess has a bow", or "the hungry princess was cold": subject must be grounded BEFORE adding the bow, so in building a full sentence world one must mark old info and new info.


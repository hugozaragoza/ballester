#Sentence Interpretation#


#### Overview

####Phases

Notation:

  * word tree (wt)
  * game world (w)

Steps:


* Parse Syn0: string -> word tree (wt0)
* Parse Syn1: wt0 -> wt1 (syntactic parsing + semantic role labelling + direct semantic parsing)
* Parse Sem1: wt2 -> w0 (sentence world)
* Parse Sem2: wt2,w0,w -> w (update the game world)

#### Parse Syn1

ADJ, DET in NP: Determinants push their existence semantics into the N and are removed

V: Word arguments of verbs are pushed as semantic features of verb and removed from sentence.

#### Parse Sem1

  * parseSem1_GroundObjects : links subj arguments to existing world objects, creates them if needed
  
  


  * parse Sem1.b : arg1,v,arg2 -> wObj : produced a sentence world describing the subject updated by the object
  * parse Sem1.c : (w,wSubj,sObj) -> w : updates the game world w
  
  > (ARG1: "The green dragon") is (ARG2: "red")
  


#####Java Implementation

* Parse0 and Parse1: `ballester.grammar.simplegrammar.parse.SimpleParser`
  * see ballester.simplegrammar.parse
* Parse2:
* Parse3: `websays.semgame.mapper.SemWorldMappper` 
* Parse3a: mapToNewAgent
* Transalte:


#####Other ideas:


**Alternative** Map world to semantic representation first and do the grounding there, instead of trying to ground directly from wt2 to game object. Problem is that we need to map entire world to sem representation, does not seem natural. Also we need sem representation of the world, harder than the sem representation of a sentence.




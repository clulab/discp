package edu.arizona.sista.discp

/**
 * Service for mocking the SISTA NLP back-end API.
 *   Last modified: Add and return mock timings.
 */
class MockApiService extends ServiceBase {

  /** Return the results from processing the given text with the given rules. */
  Map parseText (text, processor) {
    log.debug("(MockApiService.parseText): text=${text}")

    def dtJson = ['''
{"relLabel":"same-unit","kids":[{"relLabel":"elaboration","relDir":"LeftToRight","kids":[{"text":"The logical inconsistency of a Cretan asserting all Cretans are always liars may not have occurred to Epimenides , nor to Callimachus ,"},{"relLabel":"enablement","relDir":"LeftToRight","kids":[{"text":"who both used the phrase"},{"text":"to emphasize their point ,"}]}]},{"text":"without irony ."}]}''']

    def timings = '''
[["POS",   24, 123456789000, 123456789024],
 ["Lemma",  4, 123456789100, 123456789104],
 ["NER",   88, 123456789200, 123456789288],
 ["Parse",205, 123456789300, 123456789505],
 ["Chunk", 10, 123456789600, 123456789610],
 ["Roles",  0, 123456789700, 123456789700],
 ["CoRef",100, 123456789700, 123456789800],
 ["DiscP",  7, 123456789900, 123456789007]]'''

    def retMap = ['text': text, 'dTrees': [dtJson],
                  'timings': timings,
                  'synTrees': [
                    '(ROOT (S (S (NP (PRP She)) (VP (VBZ is) (NP (JJR taller)) (SBAR (IN than) (S (NP (PRP I)) (VP (VBP am)))))) (, ,) (CC but) (S (NP (PRP I)) (VP (VBP am) (ADJP (ADJP (JJR cuter)) (SBAR (IN than) (S (NP (PRP she)) (VP (VBZ is))))))) (. .)))',
                    '(ROOT (S (NP (PRP We)) (VP (VBP are) (NP (NP (DT both) (JJR taller)) (PP (IN than) (NP (NNP Brian))))) (. .)))'
                  ]]

    log.debug("(MockApiService.parseText): => ${retMap}")
    return retMap
  }

}

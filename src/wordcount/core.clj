(ns wordcount.core
  (:require 
    [cascalog.api :refer :all]
    [cascalog.logic.ops :as c]))

(defmapcatfn split
  "Accepts a sentence 1-tuple, splits that sentence on whitespace, and
   emits a single 1-tuple for each word."
  [^String sentence]
  (.split sentence "\\s+"))

(defn wordcount-query
  "Accepts a generator of lines of text and returns a subquery that
  generates a count for each word in the text sample."
  [src]
  (<- [?word ?count]
      (src ?textline)
      (split ?textline :> ?word)
      (c/count ?count)))

; rm -Rf output/passthrough && lein run -m wordcount.core.passthrough sample/words.txt output/passthrough && cat output/passthrough/part-00000
(defmain passthrough [in out]
  (?<- (hfs-textline out)
       [?line]
       ((hfs-textline in) :> ?line)))

; rm -Rf output/run && lein run -m wordcount.core.run sample/words.txt output/run && cat output/run/part-00000
(defmain run [in out]
  (?- (hfs-textline out)
      (wordcount-query (hfs-textline in))))

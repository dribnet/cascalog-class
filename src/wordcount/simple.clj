(ns wordcount.simple
  (:require 
    [clojure.string :as string]
    [cascalog.api :refer :all]
    [cascalog.logic.ops :as c]))

(defmapcatfn split
  "Accepts a sentence 1-tuple, splits that sentence on whitespace, and
   emits a single 1-tuple for each word."
  [^String sentence]
  (string/split sentence #"\s+"))

(defn wordcount-query
  "Accepts a generator of lines of text and returns a subquery that
  generates a count for each word in the text sample."
  [src]
  (<- [?word ?count]
      (src ?textline)
      (split ?textline :> ?word)
      (c/count ?count)))

; rm -Rf output/counter && lein run -m wordcount.counter.run sample/words.txt output/counter && cat output/counter/part-00000
(defmain run [in out]
  (?- (hfs-textline out)
      (wordcount-query (hfs-textline in))))

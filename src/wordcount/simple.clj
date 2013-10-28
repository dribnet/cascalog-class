(ns wordcount.simple
  (:require 
    [clojure.string :as string]
    [cascalog.api :refer :all]
    [cascalog.logic.ops :as c]
  )
)

(defmapcatfn split
  "Accepts a sentence 1-tuple, splits that sentence on whitespace, and
   emits a single 1-tuple for each word."
  [^String sentence]
  (let [
      frags (string/split (string/lower-case sentence) #"\s+")
      words (map #(second (
        re-find #"(?x)          # allow embedded whitespace and comments
                  [^a-z0-9]*    # skip until first alphanumeric
                  ([a-z0-9']+)  # grab wordy chars
                 " %)) frags)
    ]
    (remove nil? words)
  )
)

(defn wordcount-query
  "Accepts a generator of lines of text and returns a subquery that
  generates a count for each word in the text sample."
  [src]
  (<- [?word ?count]
      (src ?textline)
      (split ?textline :> ?word)
      (c/count ?count)
  )
)

; rm -Rf output/simple && lein run -m wordcount.simple.run sample/words.txt output/simple && cat output/simple/part-00000
(defmain run [in out]
  (?- (hfs-textline out)
      (wordcount-query (hfs-textline in))
  )
)

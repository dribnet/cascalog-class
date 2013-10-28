(ns wordcount.debug
  (:require 
    [clojure.string :as string]
    [cascalog.api :refer :all]
  )
)

; if you wanted a smarter split, this might do it
(defmapcatfn realsplit
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

; rm -Rf output/passthrough && lein run -m wordcount.debug.passthrough sample/words.txt output/passthrough && cat output/passthrough/part-00000
(defmain passthrough [in out]
  (?<- (hfs-textline out)
       [?line]
       ((hfs-textline in) :> ?line)
  )
)

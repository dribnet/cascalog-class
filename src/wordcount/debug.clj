(ns wordcount.debug
  (:require 
    [cascalog.api :refer :all]
  )
)

; rm -Rf output/passthrough && lein run -m wordcount.debug.passthrough sample/words.txt output/passthrough && cat output/passthrough/part-00000
(defmain passthrough [in out]
  (?<- (hfs-textline out)
       [?line]
       ((hfs-textline in) :> ?line)
  )
)

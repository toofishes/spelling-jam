(ns spelling-corrector.core
  (:require [spelling-corrector.parsers :as parsers]
            [spelling-corrector.spell :as spell]))

(def corpus (parsers/read-sample-file "/Users/ccross/dev/spelling-jam/data/big.txt"))

(defn accuracy-report-for-word [word-map]
  (let [word (first (keys word-map))
        incorrect-spellings (get word-map word)
        hits (count (filter #(= word (spell/correct corpus %)) incorrect-spellings))]
    {:misses (- (count incorrect-spellings) hits) :total (count incorrect-spellings)}))

(defn accuracy-for-test-file [file]
  (let [spellings (parsers/read-test-file file)]
    (reduce (fn [result spelling]
              (let [misses (+ (:misses result))
                    total (+ (:total result))
                    hits (- total misses)])
              {:hits hits
               :misses misses
               :total total
               :accuracy (/ hits accuracy)})
            {:misses 0 :total 0} spellings)))
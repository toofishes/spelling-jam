(ns spelling-corrector.core
  (:require [spelling-corrector.parsers :as parsers]
            [spelling-corrector.spell :as spell]))

(def corpus (parsers/read-sample-file "../data/big.txt"))

(defn accuracy-report-for-word [word-map]
  (let [word (first (keys word-map))
        incorrect-spellings (get word-map word)
        hits (count (filter #(= word (spell/correct corpus %)) incorrect-spellings))]
    {:misses (- (count incorrect-spellings) hits) :total (count incorrect-spellings)}))

(defn accuracy-for-test-file [file]
  (let [spellings (parsers/read-test-file file)]
    (reduce (fn [result spelling]
              (let [spelling-result (accuracy-report-for-word spelling)
                    misses (+ (:misses result) (:misses spelling-result))
                    total (+ (:total result) (:total spelling-result))
                    hits (- total misses)]
              {:hits hits
               :misses misses
               :total total
               :accuracy (/ hits total)}))
            {:misses 0 :total 0} spellings)))

(defn -main []
  (accuracy-for-test-file "../data/test1.txt"))

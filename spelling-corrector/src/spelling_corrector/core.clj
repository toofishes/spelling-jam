(ns spelling-corrector.core
  (:require [spelling-corrector.parsers :as parsers]
            [spelling-corrector.spell :as spell]))

(def corpus (parsers/read-sample-file "/Users/ccross/dev/spelling-jam/data/big.txt"))

(defn validate-spellings-for-word [word-map]
  (let [word (first (keys word-map))
        incorrect-spellings (get word-map word)]
    (every? #(= word (spell/correct corpus %)) incorrect-spellings)))

(defn validate-with-test-file [file]
  (let [spellings (parsers/read-test-file file)]
    (every? validate-spellings-for-word spellings)))
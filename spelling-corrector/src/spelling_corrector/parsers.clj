(ns spelling-corrector.parsers)

(defn inc-word-count [count-map word]
  (let [word-count (get count-map word)]
    (assoc count-map word (if word-count (inc word-count) 0))))

(defn word-seq [text]
  (re-seq #"[a-z]+" test-file))

(defn read-sample-file [file]
  (reduce inc-word-count {} (word-seq (slurp file))))
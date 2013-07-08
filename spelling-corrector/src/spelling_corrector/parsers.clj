(ns spelling-corrector.parsers)

(defn inc-word-count [count-map word]
  (let [word-count (get count-map word)]
    (assoc count-map word (if word-count (inc word-count) 0))))

(defn word-seq [text]
  (re-seq #"[a-z]+" text))

(defn read-sample-file [file]
  (reduce inc-word-count {} (word-seq (.toLowerCase (slurp file)))))

(defn associate-word-from-line [line]
  (when-not (= "#" (first line))
    (let [words (re-seq #"[a-z]+" line)]
      {(first words)
       (into #{} (rest words))})))

(defn word-associations [lines]
  (map associate-word-from-line lines))

(defn read-test-file [file]
  (into {} (word-associations (line-seq (clojure.java.io/reader file)))))
(ns spelling-corrector.spell)

(def alphabet "abcdefghijklmnopqrstuvwxyz")

(defn split [word]
  (let
    [splitpoints (range (+ 1 (count word)))]
    (map #(split-at % word) splitpoints)))

(defn deletes [splits]
  (map #(concat (first %) (rest (second %))) (drop-last splits)))

(defn transpose [x y]
  (concat x (str (second y) (first y)) (drop 2 y)))

(defn transposes [splits]
  (map #(transpose (first %) (second %)) (drop-last 2 splits)))

(defn insert-alphabet [x y]
  (map #(concat x (str %) y) alphabet))

(defn replaces [splits]
  (mapcat #(insert-alphabet (first %) (rest (second %))) (drop-last splits)))

(defn inserts [splits]
  (mapcat #(insert-alphabet (first %) (second %)) splits))

(defn edits [word]
  (let
    [splits (split word)]
    (set (map #(apply str %)
              (concat (deletes splits)
                      (transposes splits)
                      (replaces splits)
                      (inserts splits))))))

(defn known [corpus words]
  (filter #(contains? corpus %) words))

(defn candidates [corpus word]
  (set (concat (known corpus [word])
               (known corpus (edits word))
               (flatten (known corpus (map edits (edits word))))
               [word])))

(defn score [corpus words]
  (zipmap words (map #(get corpus % 0) words)))

(defn all-correct [corpus word]
  (sort-by #(- (val %)) (score foo (candidates corpus word))))

(defn correct [corpus word]
  (first (first (all-correct corpus word))))

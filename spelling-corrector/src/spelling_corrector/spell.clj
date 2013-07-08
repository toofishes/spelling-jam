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
  (map #(insert-alphabet (first %) (rest (second %))) (drop-last splits)))

(defn inserts [splits]
  (map #(insert-alphabet (first %) (second %)) splits))

(defn edits [word]
  (let
    [splits (split word)]
    (set (concat (deletes splits) (transposes splits) (replaces splits) (inserts splits)))))

(defn known [corpus words]
  (filter #(contains? corpus %) words))

(defn candidates [corpus word]
  (concat (known corpus [word])
          (known corpus (edits word))
          (flatten (known corpus (map edits (edits word))))
          [word]))

(defn correct [corpus word]
  )

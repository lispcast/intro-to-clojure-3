;; code you'll need loaded in the REPL

(defn scooped? [ingredient]
  (cond
    (= :flour ingredient)
    true
    (= :almond-milk ingredient)
    true
    (= :sugar ingredient)
    true
    (= :coconut-oil ingredient)
    true
    (= :baking-powder ingredient)
    true
    (= :corn-starch ingredient)
    true
    :else
    false))

(defn squeezed? [ingredient]
  (cond
    (= :lemon ingredient)
    true
    :else
    false))

(defn bowl-for [ingredient]
  (cond
    (= :flour ingredient)
    :dry
    (= :corn-starch ingredient)
    :dry
    (= :baking-powder ingredient)
    :dry
    (= :almond-milk ingredient)
    :wet
    (= :sugar ingredient)
    :wet
    (= :coconut-oil ingredient)
    :wet
    (= :lemon ingredient)
    :wet
    :else
    (error "I don't recognize" ingredient)))

(defn add-scooped
  ([ingredient]
   (add-scooped ingredient 1))
  ([ingredient n]
   (if (scooped? ingredient)
     (do
       (grab :cup)
       (dotimes [_ n]
         (scoop ingredient)
         (dump (bowl-for ingredient)))
       (release))
     (error ingredient "is not scooped."))))

(defn add-squeezed
  ([ingredient]
   (add-squeezed ingredient 1))
  ([ingredient n]
   (if (squeezed? ingredient)
     (dotimes [_ n]
       (grab ingredient)
       (squeeze (bowl-for ingredient))
       (release))
     (error ingredient "is not squeezed."))))
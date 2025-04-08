;; copy-paste this code into your REPL before trying the exercise

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

(defn add-squeezed [ingredient bowl]
  (if (squeezed? ingredient)
    (do
      (grab ingredient)
      (squeeze bowl)
      (release))
    (error ingredient "is not squeezed")))

(defn add-scooped [ingredient bowl]
  (if (scooped? ingredient)
    (do
      (grab :cup)
      (do
        (scoop ingredient)
        (dump bowl))
      (release))
    (error ingredient "is not scooped")))

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

(defn add [ingredient]
  (cond
    (squeezed? ingredient)
    (add-squeezed ingredient (bowl-for ingredient))

    (scooped? ingredient)
    (add-scooped ingredient (bowl-for ingredient))

    :else
    (error "I don't know how to add" ingredient)))

(defn add-flour-cups [n]
  (dotimes [_ n]
    (add :flour))
  :ok)

(defn add-sugar-cups [n]
  (dotimes [_ n]
    (add :sugar))
  :ok)

(defn add-almond-milk-cups [n]
  (dotimes [_ n]
    (add :almond-milk))
  :ok)

(defn add-baking-powder-cups [n]
  (dotimes [_ n]
    (add :baking-powder))
  :ok)
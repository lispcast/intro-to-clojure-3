;; The code you'll need loaded in the REPLx

(defn add-flour []
  (grab :scoop)
  (scoop :flour)
  (dump :dry)
  (release))

(defn add-almond-milk []
  (grab :scoop)
  (scoop :almond-milk)
  (dump :dry)
  (release))

(defn add-sugar []
  (grab :scoop)
  (scoop :sugar)
  (dump :wet)
  (release))

(defn add-coconut-oil []
  (grab :scoop)
  (scoop :coconut-oil)
  (dump :wet)
  (release))

(defn add-corn-starch []
  (grab :scoop)
  (scoop :corn-starch)
  (dump :dry)
  (release))

(defn add-baking-powder []
  (grab :scoop)
  (scoop :baking-powder)
  (dump :dry)
  (release))

(defn add [ingredient]
  (cond
    (= :flour ingredient)
    (add-flour)

    (= :almond-milk ingredient)
    (add-almond-milk)

    (= :sugar ingredient)
    (add-sugar)

    (= :coconut-oil ingredient)
    (add-coconut-oil)

    (= :corn-starch ingredient)
    (add-corn-starch)

    (= :baking-powder ingredient)
    (add-baking-powder)

    :else
    (error "I don't know this ingredient:" ingredient)))
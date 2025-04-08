;; We develop this code in the lesson

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

(defn bake-cookies []
  (add :flour)
  (add :corn-starch)

  (add :sugar)
  (add :coconut-oil)

  (mix-bowl :dry)
  (mix-bowl :wet)
  (pour-bowl :wet :dry)
  (mix-bowl :dry)
  (pour-bowl :dry :pan)
  (bake-pan 30)
  (cool-pan))
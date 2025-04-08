;; My solution

(defn bake-cake []
  (add-flour)
  (add-flour)
  (add-baking-flour)
  (add-almond-milk)
  (add-sugar)
  (mix-bowl :dry)
  (mix-bowl :wet)
  (pour-bowl :wet :dry)
  (mix-bowl :dry)
  (pour-bowl :dry :pan)
  (bake-pan 25)
  (cool-pan))
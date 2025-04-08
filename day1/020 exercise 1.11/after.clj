;; my solution

(defn bake-cake []
  (add :flour 2)
  (add :baking-powder)
  (add :almond-milk)
  (add :sugar)
  
  (mix-bowl :dry)
  (mix-bowl :wet)
  (pour-bowl :wet :dry)
  (mix-bowl :dry)
  (pour-bowl :dry :pan)
  (bake-pan 25)
  (cool-pan))
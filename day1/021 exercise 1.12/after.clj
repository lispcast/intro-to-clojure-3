;; my solution

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
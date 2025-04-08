;; my solution

(defn bake-cake []
  (add-flour-cups 2)
  (add-baking-powder-cups 1)
  (add-almond-milk-cups 1)
  (add-sugar-cups 1)
  
  (mix-bowl :dry)
  (mix-bowl :wet)
  (pour-bowl :wet :dry)
  (mix-bowl :dry)
  (pour-bowl :dry :pan)
  (bake-pan 25)
  (cool-pan))
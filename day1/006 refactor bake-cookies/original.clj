;; Here is the code you'll need for the new code we develop
;; copy-paste these if you need them

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

;; Here is the original definition of bake-cookies

(defn bake-cookies []
  (grab :cup)
  (scoop :flour)
  (dump :dry)
  (scoop :corn-starch)
  (dump :dry)
  (scoop :sugar)
  (dump :wet)
  (scoop :coconut-oil)
  (dump :wet)
  (mix-bowl :dry)
  (mix-bowl :wet)
  (pour-bowl :wet :dry)
  (mix :dry)
  (pour-bowl :dry :pan)
  (bake-pan 30)
  (cool-pan))
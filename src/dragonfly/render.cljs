(ns dragonfly.render
  (:require
   [dragonfly.prelude :refer [container scene]]
   [dragonfly.camera :refer [camera]]
   ["three/build/three.module.js" :as three]
   ["three/examples/jsm/postprocessing/EffectComposer.js" :refer (EffectComposer)]
   ["three/examples/jsm/postprocessing/RenderPass.js" :refer (RenderPass)]
   ["three/examples/jsm/postprocessing/OutlinePass.js" :refer (OutlinePass)]))

(defn init-render []
  (def ^js renderer (three/WebGLRenderer.))
  (.setSize renderer
            (.-clientWidth  container)
            (.-clientHeight container))
  (.appendChild container (.-domElement renderer))

  (def ^js composer (EffectComposer. renderer))
  (def ^js render-pass (RenderPass. scene camera))
  (.addPass composer render-pass)

  (def ^js outline-pass (OutlinePass. (three/Vector2. (.-clientWidth  container)
                                                      (.-clientHeight container))
                                      scene
                                      camera))
  (set! (.-visibleEdgeColor outline-pass) (three/Color. (clj->js {:color 0xffffff})))
  (set! (.-hiddenEdgeColor outline-pass) (three/Color. (clj->js {:color 0xffffff})))
  (set! (.-edgeStrength outline-pass) 5)
  (set! (.-edgeGlow outline-pass) 0)
  (set! (.-edgeThickness outline-pass) 1)
  (.addPass composer outline-pass))

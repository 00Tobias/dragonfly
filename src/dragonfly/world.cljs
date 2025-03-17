(ns dragonfly.world
  (:require
   [dragonfly.prelude :refer [scene]]
   [dragonfly.render :refer [outline-pass]]

   ["three/build/three.module.js" :as three]
   ["three/examples/jsm/loaders/GLTFLoader.js" :refer (GLTFLoader)]
   ["cannon-es/dist/cannon-es.js" :as cannon]))

(defn init-world []
  (def ^js world (cannon/World.))
  (.set (.-gravity world) 0 -9.82 0)

  ;; Ground plane
  (def ^js ground (three/Mesh. (three/PlaneGeometry. 10000 10000) (three/MeshBasicMaterial. (clj->js {:color 0x1c1c1c}))))
  (.rotateX ground (* Math/PI -0.5))
  (.add scene ground)

  ;; Ground collision
  (def ^js ground-shape (cannon/Plane.))
  (def ^js ground-body (cannon/Body. (clj->js {:mass 0})))
  (.addShape ground-body ground-shape)
  (.setFromEuler (.-quaternion ground-body) (* Math/PI -0.5) 0 0)
  (.addBody world ground-body)

  (def ^js loader (GLTFLoader.))

  (.load loader "assets/uppsala.glb"
         (fn [gltf]
           (def context (.-scene gltf))

           ;; Type-hint to help the compiler generate externs
           (defn get-object-by-name [^js/gltf.scene context name] (.getObjectByName context name))

           (set! (.-material (get-object-by-name context "buildings")) (three/MeshToonMaterial. (clj->js {:color 0x1c1c1c})))
           (set! (.-material (get-object-by-name context "river")) (three/MeshBasicMaterial. (clj->js {:color 0xffffff})))

           (set! (.-selectedObjects outline-pass) #js [(get-object-by-name context "buildings")])

           (.add scene context))))

(defn update-world []
  (.fixedStep world))

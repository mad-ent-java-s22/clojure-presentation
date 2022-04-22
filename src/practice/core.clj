(ns practice.core)
(import '(java.util Random))

;======================== syntax examples ========================
; prefix notation
(+ 2 5)                ; => 5
(> 9 7)                ; => true
; everything nests
(+ 10 (* 5 5))         ; => 35
(= 35 (+ 10 (* 5 5)))  ; => true

; define a value with def macro
; associate a symbol with a variable
(def x 3)
(def y 99)
(/ y x)               ; => 33

;======================== data types ========================
; most are taken from java
; keywords - used to reference values in hash-maps or macros


;============================= lists =============================
; construct (list function and list literal)
(def languages (list "Java" "Clojure" "Python"))
(def numList '(1 2 3))  ; quote list so reader knows not to evaluate it

; examine (first, nth, peek, .indexOf)
(first languages)             ; => "Java"
(nth languages 2)             ; => "Python"
(peek languages)              ; => "Java"
(.indexOf languages "Python") ; => 2

; "change" (conj, rest, pop)
(conj languages "C++")      ; => ("C++" "Java" "Clojure" "Python")
; diff b/w cons and conj - conj works on collections, cons works on sequences
(pop languages)               ; => ("Clojure" "Python")
(rest languages)              ; => ("Clojure" "Python")


;============================= vectors =============================
; construct
(def nums (vector 1 2 3))   ; construct with vector function
(def langs ["Java", "Clojure", "Python", "Haskell"]) ; construct as literal

; examine (nth, get, peek)
(nth langs 2)  ; => "Python"
(get nums 0)     ; => 1
(get langs 1)  ; => "Clojure"
(peek langs)   ; => "Haskell"

; "change" (assoc, pop, subvec, replace, conj, update)
(assoc nums 0 9)               ; => [9 2 3]
(assoc langs 4 "JavaScript") ; => ["Java" "Clojure" "Python" "Haskell" "JavaScript"]
(assoc langs 5 "Miranda")    ; IndexOutOfBoundsException

; pop - returns a vector without the last item
(pop langs)                  ; => ["Java" "Clojure" "Python"]

; subvec - slices an array, like java.util.subList()
(subvec langs 2)             ; => ["Python" "Haskell"]
(subvec langs 1 3)           ; => ["Clojure" "Python"]

; replace - takes a map of replacement pairs and a collection
(replace {"Java" "Clojure"} langs) ; => ["Clojure" "Clojure" "Python" "Haskell"]

; conj - add item to end of vector (same function adds to beginning of list)
(conj langs "C#")           ; => ["Java" "Clojure" "Python" "Haskell" "C#"]

; update - takes a collection, a key and a function, applies the function
; to the value associated with the key in that collection
(update nums 0 inc)         ; => [2 2 3]

;============================= sets =============================
; construct (set literal, hash-set function)
(def primes #{2 3 5 7 11})
(hash-set 1 1 2 3 4 4 4)    ; => #{1 4 3 2}

; examine (contains?)
(contains? primes 4)        ; => false
(contains? primes 3)        ; => true

; "change" (conj disj)
(conj primes 13)            ; => #{7 13 3 2 11 5}
(disj primes 2)             ; => #{7 3 11 5}


;============================= maps =============================
; construct (hash-map function, map literal)
(def meMap(hash-map :name "David"
                    :age 25
                    :occupation "IT Guide"
                    :favorite-numbers #{7 21 34}
                    :hobbies ["programming" "basketball"]
                    :pets {:dog "Rover"
                           :cat "Pumpkin"}))

{:odds [1 3 5 7 9] :evens [2 4 6 8 10]}  ; => {:odds [1 3 5 7 9], :evens [2 4 6 8 10]}

; examine (get, :key, contains, find, keys, vals, get-in)
(get meMap :name)           ; => "David"
(meMap :name)               ; => "David"
(:name meMap)               ; => "David"
(find meMap :occupation)    ; => [:occupation "IT Guide"]
(keys meMap)                ; => (:age :favorite-numbers :occupation :name :pets :weight :hobbies)
(vals meMap)                ; => (25 #{7 21 34} "IT Guide" "David" {:dog "Rover", :cat "Pumpkin"} 194 ["programming" "basketball"])
(get-in meMap [:pets :cat]) ; => "Pumpkin"


; "change" (conj, assoc, assoc-in, dissoc, merge, merge-with)
; conj - add key value pair (pass k-v pair as vector)
(conj meMap [:height 196])  ; => {:age 25, :favorite-numbers #{7 21 34},
                            ;     :occupation "IT Guide", :name "David",
                            ;     :pets {:dog "Rover", :cat "Pumpkin"},
                            ;     :hobbies ["programming" "basketball"], :height 196}

; assoc - add key value pair (pass as two separate args)
(assoc meMap :hair-color "brown")

; assoc-in - associates a value in a nested associative structure
(assoc-in meMap [:pets :lizard] "Lenny") ; => {
                          ;       :age 25,
                          ;       :favorite-numbers #{7 21 34},
                          ;       :occupation "IT Guide",
                          ;       :name "David",
                          ;       :pets {:dog "Rover", :cat "Pumpkin", :lizard "Lenny"},
                          ;       :hobbies ["programming" "basketball"]}


; dissoc - pass in key to remove that key value pair
(dissoc meMap :favorite-numbers) ; => {
                                 ; :age 25,
                                 ; :occupation "IT Guide",
                                 ; :name "David",
                                 ; :pets {:dog "Rover", :cat "Pumpkin"},
                                 ; :hobbies ["programming" "basketball"]}

; merge - merges new map k-v pairs with old map k-v pairs. If key is present in both maps new map overrides
(merge meMap {:hair-color "brown" :sign "aries" :age 44}) ; => {
                                ; :sign "aries",
                                ; :age 44,
                                ; :favorite-numbers #{7 21 34},
                                ; :occupation "IT Guide",
                                ; :name "David",
                                ; :pets {:dog "Rover", :cat "Pumpkin"},
                                ; :hair-color "brown",
                                ; :hobbies ["programming" "basketball"]}

; merge-with - pass a function and 2+ maps, conjs all key value pairs from second onto first
(merge-with +
            {:a 1 :b 2}
            {:a 9 :b 98 :c 0})  ; => {:a 10, :b 100, :c 0}



;============================= functions =============================
; regular
((fn add-one [x] (+ 1 x)) 99)   ; => 100

; using defn - defines named function
(defn double [x] (* 2 x))

(defn double
  "doubles a number"
  [x]
  (* 2 x))

; implicit returns - no return keyword - last expression evaluated is return value
; anonymous
((fn [x] (+ 1 x)) 99)           ; => 100

;; lambda
(#(+ % 1) 99)

;======================== higher-order functions ========================

; map function
; takes a function and a sequence, applies function to each element of the sequence
(map double [1 2 3 4 5])     ;=> (2 4 6 8 10)

; filter - takes a predicate and a sequence
(filter #(> % 5) '(3 4 5 6 7)) ;=> (6 7)
(filter odd? '(1 2 3 4 5 6 7)) ; => (1 3 5 7)
; some, every
(some odd? vecA)
(every? odd? vecA)

; reduce
(reduce + [1 2 3 4 5])      ; => 15
(+ 5 (+ 4 (+ 3 (+ 1 2))))

(reduce conj
        #{}
        [1 2 3 4 5])        ; => #{1 4 3 2 5}


;============================= control flow =============================
; if statement
(if (= (+ 2 2) 4)
  "two plus two is four"
  "huh?")

; cond
(def grade 85)
(cond
  (>= grade 90) "A"
  (>= grade 80) "B"
  (>= grade 70) "C"
  (>= grade 60) "D"
  :else "F")  ; => "B"

; recursion, loop & recur

; recursion
(defn print-a-collection [collection]
  (println (first collection))
  (if (empty? collection)
    (print-str "no more values to process")
    (print-a-collection (rest collection))))

(defn print-a-collection-with-recur [collection]
  (println (first collection))
  (if (empty? collection)
    (print-str "no more values to process")
    (recur (rest collection))))

; loop
(defn loop-example []
  (loop [x 10]
    (when (> x 1)
      (println x)
      (recur (- x 2)))))

(loop-example)

;========================== fizz buzz example ==========================
(defn fizz-buzz
  "the fizz buzz problem"
  []
  (loop [i 1]
    (if (<= i 21)
      (do
        (cond
          (and (= (rem i 3) 0) (= (rem i 5) 0)) (println "FizzBuzz")
          (= (rem i 3) 0) (println "Fizz")
          (= (rem i 5) 0) (println "Buzz")
          :else (println i))
        (recur (inc i))))))

;========================= thread last example =========================
(filter odd? (map inc (range 5)))   ; => (1 3 5)

; with thread last macro
(->> (range 5)                      ; => (0 1 2 3 4)
     (map inc)                      ; => (1 2 3 4 5)
     (filter odd?))                 ; => (1 3 5)

;============================= text example =============================
(def common-english-words (set (clojure.string/split (slurp "common-english-words.csv") #",")))


(def book (slurp "https://www.gutenberg.org/files/2701/2701-0.txt"))

(def words (re-seq #"[\w|']+" book))

(count words)

; 20 most frequently used (non-common) words
(->> words
     (map clojure.string/lower-case)
     (remove common-english-words)
     (frequencies)
     (sort-by val)
     (take-last 20))

; 20 longest words
(->> words
     (distinct)
     (sort-by count)
     (take-last 20)
     (group-by count)
     )

(defn palindrome?
  [collection]
  (= (seq collection) (reverse collection)))

(defn find-longest-palindrome
  "finds longest palindrome"
  [words]
  (->> words
       (distinct)
       (filter palindrome?)
       (sort-by count)
       (last)))

;============================= lookup table =============================
(def users [{:id 1
             :email "michael.lawson@gmail.com"
             :first_name "Michael"
             :last_name "Lawson"
             :avatar "https://reqres.in/img/faces/1-image.jpg"}
            {:id 2
             :email "sarah.smith@gmail.com"
             :first_name "Sarah"
             :last_name "Smith"
             :avatar "https://reqres.in/img/faces/2-image.jpg"}
            {:id 3
             :email "richard.pryor@gmail.com"
             :first_name "Richard"
             :last_name "Pryor"
             :avatar "https://reqres.in/img/faces/3-image.jpg"}
            {:id 4
             :email "sam.funke@gmail.com"
             :first_name "Sam"
             :last_name "Funke"
             :avatar "https://reqres.in/img/faces/4-image.jpg"}
            {:id 5
             :email "jenny.davidson@gmail.com"
             :first_name "Jenny"
             :last_name "Davidson"
             :avatar "https://reqres.in/img/faces/5-image.jpg"}
            {:id 6
             :email "sarah.smalls@gmail.com"
             :first_name "Sarah"
             :last_name "Smalls"
             :avatar "https://reqres.in/img/faces/6-image.jpg"}
            ])

(first (filter (fn [user]
          (= (:id user) 3)) users))

(first (filter #(= (:id %) 3) users))

(defn my-sum [total vals]
  (if (empty? vals)
    total
    (my-sum (+ (first vals) total) (rest vals))))


(ns log-collection.log-test
  (:require [clojure.test :refer :all]
            [log-collection.log :as log]))

(def test-log "resources/log")

(deftest line-matches?-works
  (testing "Works with a matching word in the middle of a sentence"
    (is (= true (#'log/line-matches? "dog" "There is a big dog"))))

  (testing "Works with a matching word in the beginning of a sentence"
    (is (= true (#'log/line-matches? "dog" "dog, there is a big one"))))

  (testing "Works when there is no match"
    (is (= false (#'log/line-matches? "cat" "dog, there is a big one"))))

  (testing "Always true when the match is empty"
    (is (= true (#'log/line-matches? "" "dog, there is a big one"))))

  (testing "Always true when the match is nil"
    (is (= true (#'log/line-matches? nil "dog, there is a big one"))))

  (testing "Ignores casing"
    (is (= true (#'log/line-matches? "dOg" "Dog, there is a big one")))
    (is (= true (#'log/line-matches? "dog" "That Dog was a big one")))))

(deftest lines-works
  (testing "No match given, and less lines requested than in the log file"
    (is (= ["log-line 5 cat" "log-line 4 dog"]
           (log/lines test-log 2 nil))))

  (testing "No match given, and more lines requested than in the log file"
    (is (= 5 (count (log/lines test-log 10 nil)))))

  (testing "Match given, and less lines requested than number of matched lines"
    (is (= ["log-line 4 dog" "log-line 3 dog"]
           (log/lines test-log 2 "dog"))))

  (testing "Match given, and more lines requested than the number of matched lines"
    (is (= ["log-line 4 dog" "log-line 3 dog" "log-line 1 dog"]
           (log/lines test-log 10 "dog"))))

  (testing "Match not found"
    (is (= []
           (log/lines test-log 10 "doggy"))))

  (testing "Match with a different case given"
    (is (= ["log-line 4 dog" "log-line 3 dog" "log-line 1 dog"]
           (log/lines test-log 10 "DoG"))))

  (testing "It only executes the filter until the sequence contains N values"
    (let [log-matches? log/line-matches?
          n-times-filter-executed (atom 0)]
      (with-redefs [log/line-matches? (fn [match line]
                                        (swap! n-times-filter-executed inc)
                                        (log-matches? match line))]

        (is (= ["log-line 5 cat" "log-line 4 dog"] (log/lines test-log 2 nil)))

        ;; It only had to look into 2 lines of the log
        (is (= 2 @n-times-filter-executed))

        (reset! n-times-filter-executed 0)

        (is (= ["log-line 4 dog" "log-line 3 dog"] (log/lines test-log 2 "dog")))

        ;; This time it had to look into 3 lines of the log
        (is (= 3 @n-times-filter-executed))))))
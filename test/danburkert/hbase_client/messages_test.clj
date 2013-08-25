(ns danburkert.hbase-client.messages-test
  (:require [danburkert.hbase-client.messages :refer :all]
            [clojure.test :refer :all])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream]))

(def msg (create RequestHeader {:call-id 0
                                 :method-name "MethodName"
                                 :request-param false}))

(def serialized-bytes
  (let [out (java.io.ByteArrayOutputStream.)]
    (write! msg out)
    (.toByteArray out)))

(def delimited-bytes
  (let [out (java.io.ByteArrayOutputStream.)]
    (write-delimited! msg out)
    (.toByteArray out)))

(deftest test-size-funcs
  (testing "size"
    (is (= (alength serialized-bytes)
           (size msg))))
  (testing "delimited size"
    (is (= (alength delimited-bytes)
           (delimited-size msg)))))

(deftest test-read-write
  (testing "non-delimited serde"
    (let [os (ByteArrayOutputStream.)]
      (write! msg os)
      (is (= msg (read! RequestHeader (ByteArrayInputStream. (.toByteArray os)))))))
  (testing "delimited serde"
    (let [os (ByteArrayOutputStream.)]
      (write-delimited! msg os)
      (is (= msg (read-delimited! RequestHeader (ByteArrayInputStream. (.toByteArray os))))))))

(comment
  (run-tests)
  )

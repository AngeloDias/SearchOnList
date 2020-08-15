package br.com.training.android.searchonlist.utils

class CheckPartialPermutation {

    companion object {

        /**
         * This solution assumes that the strings provided will be in lower case.
         * Therefore, no case-sensitive checks have been implemented.
         * */
        fun stringHaveAtMostOnePermutation(defaultStr: String, checkingStr: String): Boolean {
            if ((defaultStr.isEmpty() || checkingStr.isEmpty())
                || defaultStr.length != checkingStr.length
                || defaultStr[0] != checkingStr[0]
            ) {
                return false
            }

            var countDiff = 0

            /**
             * To determine if a checking string is permutation it's a must to count the amount of
             * letters displaced.
             * */
            for (i in defaultStr.indices) {
                if (checkingStr[i] != defaultStr[i] && i + 1 < defaultStr.length
                    && checkingStr[i] != defaultStr[i + 1]
                ) {
                    countDiff++
                }

                if (countDiff > 2) {
                    return false
                }
            }

            return true
        }

    }

}

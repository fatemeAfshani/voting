package org.voting.poll.domain.poll

class PriceCalculator {
}
//base_price_per_voter = 0.10 USD
//question_multiplier = 1 + (num_questions × 0.05)
//preference_multiplier = 1 + (num_rare_preferences × 0.1)
//
//estimated_price = expected_voters × base_price_per_voter × question_multiplier × preference_multiplier
//

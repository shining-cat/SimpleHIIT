package fr.shiningcat.simplehiit.commonresources.helpers

import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.Exercise

class ExerciseGifMapper {
    fun map(exercise: Exercise): Int =
        when (exercise) {
            Exercise.CatBirdDogs -> R.raw.exercise_cat_bird_dogs
            Exercise.CatKneeAndElbowPressUp -> R.raw.exercise_cat_knee_and_elbow_pressup
            Exercise.CatKneePushUp -> R.raw.exercise_cat_knee_pushup
            Exercise.CatBackLegLift -> R.raw.exercise_cat_back_leg_lift
            Exercise.CatDonkeyKickTwist -> R.raw.exercise_cat_donkey_kick_twist
            //
            Exercise.CrabAdvancedBridge -> R.raw.exercise_crab_advanced_bridge
            Exercise.CrabBridgeAndTwist -> R.raw.exercise_crab_bridge_twist
            Exercise.CrabKicks -> R.raw.exercise_crab_kicks
            Exercise.CrabToeTouches -> R.raw.exercise_crab_toe_touches
            Exercise.CrabSingleLegTricepsDips -> R.raw.exercise_crab_single_leg_triceps_dips
            //
            Exercise.LungesAlternateFrontRaise -> R.raw.exercise_lunge_alternate_front_raise
            Exercise.LungesAlternateSideTouch -> R.raw.exercise_lunge_alternate_side_touch
            Exercise.LungesArmsCrossSide -> R.raw.exercise_lunge_arms_cross_side
            Exercise.LungesAlternateCurtsy -> R.raw.exercise_lunge_alternate_curtsy
            Exercise.LungesTwist -> R.raw.exercise_lunge_twist
            Exercise.LungesBasic -> R.raw.exercise_lunge_basic
            Exercise.LungesShouldersSqueezeReverse -> R.raw.exercise_lunge_shoulders_squeeze_reverse
            Exercise.LungesCurtsySideKickLateralRaise -> R.raw.exercise_lunge_curtsy_side_kick_lateral_raise
            Exercise.LungesCurtsySideKick -> R.raw.exercise_lunge_curtsy_side_kick
            Exercise.LungesFrontAndBack -> R.raw.exercise_lunge_front_and_back
            Exercise.LungesBackKick -> R.raw.exercise_lunge_back_kick
            Exercise.LungesFrontKick -> R.raw.exercise_lunge_front_kick
            Exercise.LungesSideToCurtsy -> R.raw.exercise_lunge_side_to_curtsy
            Exercise.LungesSide -> R.raw.exercise_lunge_side
            Exercise.LungesSplitSquat -> R.raw.exercise_lunge_split_squat
            //
            Exercise.LyingDeadBug -> R.raw.exercise_lying_dead_bug
            Exercise.LyingReverseCrunches -> R.raw.exercise_lying_reverse_crunches
            Exercise.LyingScissorKicks -> R.raw.exercise_lying_scissor_kicks
            Exercise.LyingStarToeTouchSitUp -> R.raw.exercise_lying_star_toe_touch_sit_up
            Exercise.LyingSupermanTwist -> R.raw.exercise_lying_superman_twist
            Exercise.LyingSideLegLift -> R.raw.exercise_lying_side_leg_lift
            //
            Exercise.PlankMountainClimberTwist -> R.raw.exercise_plank_mountain_climber_twist
            Exercise.PlankMountainClimber -> R.raw.exercise_plank_mountain_climber
            Exercise.PlankBirdDogs -> R.raw.exercise_plank_bird_dogs
            Exercise.PlankShoulderTap -> R.raw.exercise_plank_shoulder_tap
            Exercise.PlankReverseLegRaise -> R.raw.exercise_plank_reverse_leg_raise
            Exercise.PlankSpiderman -> R.raw.exercise_plank_spiderman
            Exercise.PlankKneeToElbowKickback -> R.raw.exercise_plank_knee_to_elbow_kickback
            Exercise.PlankSideArmRotation -> R.raw.exercise_plank_side_arm_rotation
            //
            Exercise.SittingBoatTwist -> R.raw.exercise_sitting_boat_twist
            Exercise.SittingKneeHugs -> R.raw.exercise_sitting_knee_hugs
            Exercise.SittingRopeClimbCrunches -> R.raw.exercise_sitting_rope_climb_crunches
            Exercise.SittingTouchFloorTwist -> R.raw.exercise_sitting_touch_floor_twist
            Exercise.SittingKneeTucks -> R.raw.exercise_sitting_knee_tucks
            Exercise.SittingTwistBicycle -> R.raw.exercise_sitting_twist_bicycle
            //
            Exercise.SquatBoxerPunch -> R.raw.exercise_squat_boxer_punch
            Exercise.SquatCossack -> R.raw.exercise_squat_cossack
            Exercise.SquatHalfCrossJab -> R.raw.exercise_squat_half_cross_jab
            Exercise.SquatKickBack -> R.raw.exercise_squat_kick_back
            Exercise.SquatOverheadTricepsExtension -> R.raw.exercise_squat_overhead_triceps_extension
            Exercise.SquatBasic -> R.raw.exercise_squat_basic
            Exercise.SquatSumo -> R.raw.exercise_squat_sumo
            Exercise.SquatBaseballBatHit -> R.raw.exercise_squat_baseball_bat_hit
            //
            Exercise.StandingBentOverTwist -> R.raw.exercise_standing_bent_over_twist
            Exercise.StandingTouchFeetJacks -> R.raw.exercise_standing_touch_feet_jacks
            Exercise.StandingKickCrunches -> R.raw.exercise_standing_kick_crunches
            Exercise.StandingSkater -> R.raw.exercise_standing_skater
            Exercise.StandingSkatingWindmill -> R.raw.exercise_standing_skating_windmill
            Exercise.StandingCrissCrossCrunches -> R.raw.exercise_standing_criss_cross_crunches
            Exercise.StandingMountainClimber -> R.raw.exercise_standing_mountain_climber
            Exercise.StandingBalanceChops -> R.raw.exercise_standing_balance_chops
        }
}

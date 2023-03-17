package fr.shining_cat.simplehiit.ui.helpers

import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.R

class ExerciseDisplayNameMapper {

    fun map(exercise:Exercise):Int{
        return when(exercise){
            Exercise.CatBirdDogs -> R.string.exercise_cat_bird_dogs
            Exercise.CatKneeAndElbowPressUp -> R.string.exercise_cat_knee_and_elbow_pressup
            Exercise.CatKneePushUp -> R.string.exercise_cat_knee_pushup
            Exercise.CatBackLegLift -> R.string.exercise_cat_back_leg_lift
            Exercise.CatDonkeyKickTwist -> R.string.exercise_cat_donkey_kick_twist
            //
            Exercise.CrabAdvancedBridge -> R.string.exercise_crab_advanced_bridge
            Exercise.CrabBridgeAndTwist -> R.string.exercise_crab_bridge_twist
            Exercise.CrabKicks -> R.string.exercise_crab_kicks
            Exercise.CrabToeTouches -> R.string.exercise_crab_toe_touches
            Exercise.CrabSingleLegTricepsDips -> R.string.exercise_crab_single_leg_triceps_dips
            //
            Exercise.LungesAlternateFrontRaise -> R.string.exercise_lunge_alternate_front_raise
            Exercise.LungesAlternateSideTouch -> R.string.exercise_lunge_alternate_side_touch
            Exercise.LungesArmsCrossSide -> R.string.exercise_lunge_arms_cross_side
            Exercise.LungesAlternateCurtsy -> R.string.exercise_lunge_alternate_curtsy
            Exercise.LungesTwist -> R.string.exercise_lunge_twist
            Exercise.LungesBasic -> R.string.exercise_lunge_basic
            Exercise.LungesShouldersSqueezeReverse -> R.string.exercise_lunge_shoulders_squeeze_reverse
            Exercise.LungesCurtsySideKickLateralRaise -> R.string.exercise_lunge_curtsy_side_kick_lateral_raise
            Exercise.LungesCurtsySideKick -> R.string.exercise_lunge_curtsy_side_kick
            Exercise.LungesFrontAndBack -> R.string.exercise_lunge_front_and_back
            Exercise.LungesBackKick -> R.string.exercise_lunge_back_kick
            Exercise.LungesFrontKick -> R.string.exercise_lunge_front_kick
            Exercise.LungesSideToCurtsy -> R.string.exercise_lunge_side_to_curtsy
            Exercise.LungesSide -> R.string.exercise_lunge_side
            Exercise.LungesSplitSquat -> R.string.exercise_lunge_split_squat
            //
            Exercise.LyingDeadBug -> R.string.exercise_lying_dead_bug
            Exercise.LyingReverseCrunches -> R.string.exercise_lying_reverse_crunches
            Exercise.LyingScissorKicks -> R.string.exercise_lying_scissor_kicks
            Exercise.LyingStarToeTouchSitUp -> R.string.exercise_lying_star_toe_touch_sit_up
            Exercise.LyingSupermanTwist -> R.string.exercise_lying_superman_twist
            Exercise.LyingSideLegLift -> R.string.exercise_lying_side_leg_lift
            //
            Exercise.PlankMountainClimberTwist -> R.string.exercise_plank_mountain_climber_twist
            Exercise.PlankMountainClimber -> R.string.exercise_plank_mountain_climber
            Exercise.PlankBirdDogs -> R.string.exercise_plank_bird_dogs
            Exercise.PlankShoulderTap -> R.string.exercise_plank_shoulder_tap
            Exercise.PlankReverseLegRaise -> R.string.exercise_plank_reverse_leg_raise
            Exercise.PlankSpiderman -> R.string.exercise_plank_spiderman
            Exercise.PlankKneeToElbowKickback -> R.string.exercise_plank_knee_to_elbow_kickback
            Exercise.PlankSideArmRotation -> R.string.exercise_plank_side_arm_rotation
            //
            Exercise.SittingBoatTwist -> R.string.exercise_sitting_boat_twist
            Exercise.SittingKneeHugs -> R.string.exercise_sitting_knee_hugs
            Exercise.SittingRopeClimbCrunches -> R.string.exercise_sitting_rope_climb_crunches
            Exercise.SittingTouchFloorTwist -> R.string.exercise_sitting_touch_floor_twist
            Exercise.SittingKneeTucks -> R.string.exercise_sitting_knee_tucks
            Exercise.SittingTwistBicycle -> R.string.exercise_sitting_twist_bicycle
            //
            Exercise.SquatBoxerPunch -> R.string.exercise_squat_boxer_punch
            Exercise.SquatCossack -> R.string.exercise_squat_cossack
            Exercise.SquatHalfCrossJab -> R.string.exercise_squat_half_cross_jab
            Exercise.SquatKickBack -> R.string.exercise_squat_kick_back
            Exercise.SquatOverheadTricepsExtension -> R.string.exercise_squat_overhead_triceps_extension
            Exercise.SquatBasic -> R.string.exercise_squat_basic
            Exercise.SquatSumo -> R.string.exercise_squat_sumo
            Exercise.SquatBaseballBatHit -> R.string.exercise_squat_baseball_bat_hit
            //
            Exercise.StandingBentOverTwist -> R.string.exercise_standing_bent_over_twist
            Exercise.StandingTouchFeetJacks -> R.string.exercise_standing_touch_feet_jacks
            Exercise.StandingKickCrunches -> R.string.exercise_standing_kick_crunches
            Exercise.StandingSkater -> R.string.exercise_standing_skater
            Exercise.StandingSkatingWindmill -> R.string.exercise_standing_skating_windmill
            Exercise.StandingCrissCrossCrunches -> R.string.exercise_standing_criss_cross_crunches
            Exercise.StandingMountainClimber -> R.string.exercise_standing_mountain_climber
            Exercise.StandingBalanceChops -> R.string.exercise_standing_balance_chops
        }
    }

}
package fr.shining_cat.simplehiit.domain.models

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.models.ExerciseType.*

@ExcludeFromJacocoGeneratedReport
enum class ExerciseType{
    CAT, CRAB, LUNGE, LYING, PLANK, SITTING, SQUAT, STANDING
}

@ExcludeFromJacocoGeneratedReport
sealed class Exercise(
    val exerciseType:ExerciseType,
    val asymmetrical: Boolean,
    val uniqueName: String
) {
    object CatBirdDogs : Exercise(exerciseType = CAT, asymmetrical = false, uniqueName = "exercise_cat_bird_dogs")
    object CatKneeAndElbowPressUp : Exercise(exerciseType = CAT, asymmetrical = false, uniqueName = "exercise_cat_knee_and_elbow_pressup")
    object CatKneePushUp : Exercise(exerciseType = CAT, asymmetrical = false, uniqueName = "exercise_cat_knee_pushup")
    object CatBackLegLift : Exercise(exerciseType = CAT, asymmetrical = true, uniqueName = "exercise_cat_back_leg_lift")
    object CatDonkeyKickTwist : Exercise(exerciseType = CAT, asymmetrical = true, uniqueName = "exercise_cat_donkey_kick_twist")
    //
    object CrabAdvancedBridge : Exercise(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_advanced_bridge")
    object CrabBridgeAndTwist : Exercise(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_bridge_twist")
    object CrabKicks : Exercise(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_kicks")
    object CrabToeTouches : Exercise(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_toe_touches")
    object CrabSingleLegTricepsDips : Exercise(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_single_leg_triceps_dips")
    //
    object LungesAlternateFrontRaise : Exercise(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_alternate_front_raise")
    object LungesAlternateSideTouch : Exercise(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_alternate_side_touch")
    object LungesArmsCrossSide : Exercise(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_arms_cross_side")
    object LungesAlternateCurtsy : Exercise(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_alternate_curtsy")
    object LungesTwist : Exercise(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_twist")
    object LungesBasic : Exercise(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_basic")
    object LungesShouldersSqueezeReverse : Exercise(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_shoulders_squeeze_reverse")
    object LungesCurtsySideKickLateralRaise : Exercise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_curtsy_side_kick_lateral_raise")
    object LungesCurtsySideKick : Exercise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_curtsy_side_kick")
    object LungesFrontAndBack : Exercise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_front_and_back")
    object LungesBackKick : Exercise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_back_kick")
    object LungesFrontKick : Exercise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_front_kick")
    object LungesSideToCurtsy : Exercise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_side_to_curtsy")
    object LungesSide : Exercise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_side")
    object LungesSplitSquat : Exercise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_split_squat")
    //
    object LyingDeadBug : Exercise(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_dead_bug")
    object LyingReverseCrunches : Exercise(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_reverse_crunches")
    object LyingScissorKicks : Exercise(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_scissor_kicks")
    object LyingStarToeTouchSitUp : Exercise(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_star_toe_touch_sit_up")
    object LyingSupermanTwist : Exercise(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_superman_twist")
    object LyingSideLegLift : Exercise(exerciseType = LYING, asymmetrical = true, uniqueName = "exercise_lying_side_leg_lift")
    //
    object PlankMountainClimberTwist : Exercise(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_mountain_climber_twist")
    object PlankMountainClimber : Exercise(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_mountain_climber")
    object PlankBirdDogs : Exercise(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_bird_dogs")
    object PlankShoulderTap : Exercise(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_shoulder_tap")
    object PlankReverseLegRaise : Exercise(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_reverse_leg_raise")
    object PlankSpiderman : Exercise(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_spiderman")
    object PlankKneeToElbowKickback : Exercise(exerciseType = PLANK, asymmetrical = true, uniqueName = "exercise_plank_knee_to_elbow_kickback")
    object PlankSideArmRotation : Exercise(exerciseType = PLANK, asymmetrical = true, uniqueName = "exercise_plank_side_arm_rotation")
    //
    object SittingBoatTwist : Exercise(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_boat_twist")
    object SittingKneeHugs : Exercise(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_knee_hugs")
    object SittingRopeClimbCrunches : Exercise(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_rope_climb_crunches")
    object SittingTouchFloorTwist : Exercise(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_touch_floor_twist")
    object SittingKneeTucks : Exercise(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_knee_tucks")
    object SittingTwistBicycle : Exercise(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_twist_bicycle")
    //
    object SquatBoxerPunch : Exercise(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_boxer_punch")
    object SquatCossack : Exercise(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_cossack")
    object SquatHalfCrossJab : Exercise(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_half_cross_jab")
    object SquatKickBack : Exercise(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_kick_back")
    object SquatOverheadTricepsExtension: Exercise(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_overhead_triceps_extension")
    object SquatBasic: Exercise(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_basic")
    object SquatSumo: Exercise(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_sumo")
    object SquatBaseballBatHit: Exercise(exerciseType = SQUAT, asymmetrical = true, uniqueName = "exercise_squat_baseball_bat_hit")
    //
    object StandingBentOverTwist: Exercise(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_bent_over_twist")
    object StandingTouchFeetJacks: Exercise(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_touch_feet_jacks")
    object StandingKickCrunches: Exercise(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_kick_crunches")
    object StandingSkater: Exercise(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_skater")
    object StandingSkatingWindmill: Exercise(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_skating_windmill")
    object StandingCrissCrossCrunches: Exercise(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_criss_cross_crunches")
    object StandingMountainClimber: Exercise(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_mountain_climber")
    object StandingBalanceChops: Exercise(exerciseType = STANDING, asymmetrical = true, uniqueName = "exercise_standing_balance_chops")
}
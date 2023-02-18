package fr.shining_cat.simplehiit.domain.models

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.models.ExerciseType.*

/**
 *  The order of exercises types set here is what will determine the order in practice
 */
@ExcludeFromJacocoGeneratedReport
enum class ExerciseType{
    STANDING, SQUAT, CAT, PLANK, SITTING, CRAB, LUNGE, LYING
}

@ExcludeFromJacocoGeneratedReport
enum class Exercise(
    val exerciseType:ExerciseType,
    val asymmetrical: Boolean,
    val uniqueName: String
) {
     CatBirdDogs(exerciseType = CAT, asymmetrical = false, uniqueName = "exercise_cat_bird_dogs"),
     CatKneeAndElbowPressUp(exerciseType = CAT, asymmetrical = false, uniqueName = "exercise_cat_knee_and_elbow_pressup"),
     CatKneePushUp(exerciseType = CAT, asymmetrical = false, uniqueName = "exercise_cat_knee_pushup"),
     CatBackLegLift(exerciseType = CAT, asymmetrical = true, uniqueName = "exercise_cat_back_leg_lift"),
     CatDonkeyKickTwist(exerciseType = CAT, asymmetrical = true, uniqueName = "exercise_cat_donkey_kick_twist"),
    //
     CrabAdvancedBridge(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_advanced_bridge"),
     CrabBridgeAndTwist(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_bridge_twist"),
     CrabKicks(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_kicks"),
     CrabToeTouches(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_toe_touches"),
     CrabSingleLegTricepsDips(exerciseType = CRAB, asymmetrical = false, uniqueName = "exercise_crab_single_leg_triceps_dips"),
    //
     LungesAlternateFrontRaise(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_alternate_front_raise"),
     LungesAlternateSideTouch(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_alternate_side_touch"),
     LungesArmsCrossSide(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_arms_cross_side"),
     LungesAlternateCurtsy(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_alternate_curtsy"),
     LungesTwist(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_twist"),
     LungesBasic(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_basic"),
     LungesShouldersSqueezeReverse(exerciseType = LUNGE, asymmetrical = false, uniqueName = "exercise_lunge_shoulders_squeeze_reverse"),
     LungesCurtsySideKickLateralRaise(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_curtsy_side_kick_lateral_raise"),
     LungesCurtsySideKick(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_curtsy_side_kick"),
     LungesFrontAndBack(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_front_and_back"),
     LungesBackKick(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_back_kick"),
     LungesFrontKick(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_front_kick"),
     LungesSideToCurtsy(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_side_to_curtsy"),
     LungesSide(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_side"),
     LungesSplitSquat(exerciseType = LUNGE, asymmetrical = true, uniqueName = "exercise_lunge_split_squat"),
    //
     LyingDeadBug(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_dead_bug"),
     LyingReverseCrunches(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_reverse_crunches"),
     LyingScissorKicks(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_scissor_kicks"),
     LyingStarToeTouchSitUp(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_star_toe_touch_sit_up"),
     LyingSupermanTwist(exerciseType = LYING, asymmetrical = false, uniqueName = "exercise_lying_superman_twist"),
     LyingSideLegLift(exerciseType = LYING, asymmetrical = true, uniqueName = "exercise_lying_side_leg_lift"),
    //
     PlankMountainClimberTwist(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_mountain_climber_twist"),
     PlankMountainClimber(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_mountain_climber"),
     PlankBirdDogs(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_bird_dogs"),
     PlankShoulderTap(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_shoulder_tap"),
     PlankReverseLegRaise(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_reverse_leg_raise"),
     PlankSpiderman(exerciseType = PLANK, asymmetrical = false, uniqueName = "exercise_plank_spiderman"),
     PlankKneeToElbowKickback(exerciseType = PLANK, asymmetrical = true, uniqueName = "exercise_plank_knee_to_elbow_kickback"),
     PlankSideArmRotation(exerciseType = PLANK, asymmetrical = true, uniqueName = "exercise_plank_side_arm_rotation"),
    //
     SittingBoatTwist(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_boat_twist"),
     SittingKneeHugs(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_knee_hugs"),
     SittingRopeClimbCrunches(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_rope_climb_crunches"),
     SittingTouchFloorTwist(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_touch_floor_twist"),
     SittingKneeTucks(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_knee_tucks"),
     SittingTwistBicycle(exerciseType = SITTING, asymmetrical = false, uniqueName = "exercise_sitting_twist_bicycle"),
    //
     SquatBoxerPunch(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_boxer_punch"),
     SquatCossack(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_cossack"),
     SquatHalfCrossJab(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_half_cross_jab"),
     SquatKickBack(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_kick_back"),
     SquatOverheadTricepsExtension(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_overhead_triceps_extension"),
     SquatBasic(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_basic"),
     SquatSumo(exerciseType = SQUAT, asymmetrical = false, uniqueName = "exercise_squat_sumo"),
     SquatBaseballBatHit(exerciseType = SQUAT, asymmetrical = true, uniqueName = "exercise_squat_baseball_bat_hit"),
    //
     StandingBentOverTwist(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_bent_over_twist"),
     StandingTouchFeetJacks(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_touch_feet_jacks"),
     StandingKickCrunches(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_kick_crunches"),
     StandingSkater(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_skater"),
     StandingSkatingWindmill(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_skating_windmill"),
     StandingCrissCrossCrunches(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_criss_cross_crunches"),
     StandingMountainClimber(exerciseType = STANDING, asymmetrical = false, uniqueName = "exercise_standing_mountain_climber"),
     StandingBalanceChops(exerciseType = STANDING, asymmetrical = true, uniqueName = "exercise_standing_balance_chops")
}
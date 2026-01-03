package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.domain.common.models.ExerciseType.CAT
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType.CRAB
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType.LUNGE
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType.LYING
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType.PLANK
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType.SITTING
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType.SQUAT
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType.STANDING

/**
 *  The order of exercises types set here is what will determine the order in practice
 */
enum class ExerciseType {
    STANDING,
    SQUAT,
    CAT,
    PLANK,
    SITTING,
    CRAB,
    LUNGE,
    LYING,
}

enum class Exercise(
    val exerciseType: ExerciseType,
    val asymmetrical: Boolean,
) {
    CatBirdDogs(exerciseType = CAT, asymmetrical = false),
    CatKneeAndElbowPressUp(exerciseType = CAT, asymmetrical = false),
    CatKneePushUp(exerciseType = CAT, asymmetrical = false),
    CatBackLegLift(exerciseType = CAT, asymmetrical = true),
    CatDonkeyKickTwist(exerciseType = CAT, asymmetrical = true),

    //
    CrabAdvancedBridge(exerciseType = CRAB, asymmetrical = false),
    CrabBridgeAndTwist(exerciseType = CRAB, asymmetrical = false),
    CrabKicks(exerciseType = CRAB, asymmetrical = false),
    CrabToeTouches(exerciseType = CRAB, asymmetrical = false),
    CrabSingleLegTricepsDips(exerciseType = CRAB, asymmetrical = true),

    //
    LungesAlternateFrontRaise(exerciseType = LUNGE, asymmetrical = false),
    LungesAlternateSideTouch(exerciseType = LUNGE, asymmetrical = false),
    LungesArmsCrossSide(exerciseType = LUNGE, asymmetrical = false),
    LungesAlternateCurtsy(exerciseType = LUNGE, asymmetrical = false),
    LungesTwist(exerciseType = LUNGE, asymmetrical = false),
    LungesBasic(exerciseType = LUNGE, asymmetrical = false),
    LungesShouldersSqueezeReverse(exerciseType = LUNGE, asymmetrical = false),
    LungesCurtsySideKickLateralRaise(exerciseType = LUNGE, asymmetrical = true),
    LungesCurtsySideKick(exerciseType = LUNGE, asymmetrical = true),
    LungesFrontAndBack(exerciseType = LUNGE, asymmetrical = true),
    LungesBackKick(exerciseType = LUNGE, asymmetrical = true),
    LungesFrontKick(exerciseType = LUNGE, asymmetrical = true),
    LungesSideToCurtsy(exerciseType = LUNGE, asymmetrical = true),
    LungesSide(exerciseType = LUNGE, asymmetrical = true),
    LungesSplitSquat(exerciseType = LUNGE, asymmetrical = true),

    //
    LyingDeadBug(exerciseType = LYING, asymmetrical = false),
    LyingReverseCrunches(exerciseType = LYING, asymmetrical = false),
    LyingScissorKicks(exerciseType = LYING, asymmetrical = false),
    LyingStarToeTouchSitUp(exerciseType = LYING, asymmetrical = false),
    LyingSupermanTwist(exerciseType = LYING, asymmetrical = false),
    LyingSideLegLift(exerciseType = LYING, asymmetrical = true),

    //
    PlankMountainClimberTwist(exerciseType = PLANK, asymmetrical = false),
    PlankMountainClimber(exerciseType = PLANK, asymmetrical = false),
    PlankBirdDogs(exerciseType = PLANK, asymmetrical = false),
    PlankShoulderTap(exerciseType = PLANK, asymmetrical = false),
    PlankReverseLegRaise(exerciseType = PLANK, asymmetrical = false),
    PlankSpiderman(exerciseType = PLANK, asymmetrical = false),
    PlankKneeToElbowKickback(exerciseType = PLANK, asymmetrical = true),
    PlankSideArmRotation(exerciseType = PLANK, asymmetrical = true),

    //
    SittingBoatTwist(exerciseType = SITTING, asymmetrical = false),
    SittingKneeHugs(exerciseType = SITTING, asymmetrical = false),
    SittingRopeClimbCrunches(exerciseType = SITTING, asymmetrical = false),
    SittingTouchFloorTwist(exerciseType = SITTING, asymmetrical = false),
    SittingKneeTucks(exerciseType = SITTING, asymmetrical = false),
    SittingTwistBicycle(exerciseType = SITTING, asymmetrical = false),

    //
    SquatBoxerPunch(exerciseType = SQUAT, asymmetrical = false),
    SquatCossack(exerciseType = SQUAT, asymmetrical = false),
    SquatHalfCrossJab(exerciseType = SQUAT, asymmetrical = false),
    SquatKickBack(exerciseType = SQUAT, asymmetrical = false),
    SquatOverheadTricepsExtension(exerciseType = SQUAT, asymmetrical = false),
    SquatBasic(exerciseType = SQUAT, asymmetrical = false),
    SquatSumo(exerciseType = SQUAT, asymmetrical = false),
    SquatBaseballBatHit(exerciseType = SQUAT, asymmetrical = true),

    //
    StandingBentOverTwist(exerciseType = STANDING, asymmetrical = false),
    StandingTouchFeetJacks(exerciseType = STANDING, asymmetrical = false),
    StandingKickCrunches(exerciseType = STANDING, asymmetrical = false),
    StandingSkater(exerciseType = STANDING, asymmetrical = false),
    StandingSkatingWindmill(exerciseType = STANDING, asymmetrical = false),
    StandingCrissCrossCrunches(exerciseType = STANDING, asymmetrical = false),
    StandingMountainClimber(exerciseType = STANDING, asymmetrical = false),
    StandingBalanceChops(exerciseType = STANDING, asymmetrical = true),
}

package io.wear.tether

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.ConfirmationActivity
import android.view.WindowManager

import com.google.android.gms.common.api.GoogleApiClient

import io.wear.tether.messages.ConfigurationModel
import io.wear.tether.state.ChangeHotspotState
import io.state.machine.StateMachine
import io.state.machine.Stateful

public abstract class BaseHotspotActivity(public val requestToSend: String) : Activity(), Stateful<ChangeHotspotState> {
    public var googleApiClient: GoogleApiClient? = null
    protected var stateMachine: StateMachine<ChangeHotspotState> = StateMachine(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Activity>.onCreate(savedInstanceState)

        setContentView(R.layout.progress_view)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


    override fun setState(state: ChangeHotspotState) {
        stateMachine.setState(state)
    }

    public fun showFailure(message: String) {
        showConfirmation(ConfirmationActivity.FAILURE_ANIMATION, message)
    }

    public fun showSuccess(message: String) {
        showConfirmation(ConfirmationActivity.SUCCESS_ANIMATION, message)
    }

    private fun showConfirmation(type: Int, message: String) {
        val intent = Intent(this, javaClass<ConfirmationActivity>())
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, type)
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message)
        startActivity(intent)
    }

    public abstract fun onSuccess(config: ConfigurationModel?)
}

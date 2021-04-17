package nok.terceiro.sensorapplication

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import nok.terceiro.sensorapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.acelerometro.setOnClickListener {
            sensorManager.unregisterListener(this)
            binding.tAccel.isVisible = true
            binding.tProx.isVisible = false
            binding.tLuz.isVisible = false
            binding.tGiro.isVisible = false
            setUpSensorStuff(1)
        }

        binding.proximidade.setOnClickListener {
            sensorManager.unregisterListener(this)
            binding.tAccel.isVisible = false
            binding.tProx.isVisible = true
            binding.tLuz.isVisible = false
            binding.tGiro.isVisible = false
            setUpSensorStuff(2)
        }

        binding.luminosidade.setOnClickListener {
            sensorManager.unregisterListener(this)
            binding.tAccel.isVisible = false
            binding.tProx.isVisible = false
            binding.tLuz.isVisible = true
            binding.tGiro.isVisible = false
            setUpSensorStuff(3)
        }

        binding.giroscopio.setOnClickListener {
            sensorManager.unregisterListener(this)
            binding.tAccel.isVisible = false
            binding.tProx.isVisible = false
            binding.tLuz.isVisible = false
            binding.tGiro.isVisible = true
            setUpSensorStuff(4)
        }

        setUpSensorStuff(1)
    }

    private fun setUpSensorStuff(sensor: Int) {
        when(sensor){
            1 -> {
                sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
                    sensorManager.registerListener(
                        this,
                        it,
                        SensorManager.SENSOR_DELAY_FASTEST,
                        SensorManager.SENSOR_DELAY_FASTEST)
                }
            }
            2 -> {
                sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)?.also {
                    sensorManager.registerListener(
                        this,
                        it,
                        SensorManager.SENSOR_DELAY_FASTEST,
                        SensorManager.SENSOR_DELAY_FASTEST)
                }
            }
            3 -> {
                sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.also {
                    sensorManager.registerListener(
                        this,
                        it,
                        SensorManager.SENSOR_DELAY_FASTEST,
                        SensorManager.SENSOR_DELAY_FASTEST)
                }
            }
            4 -> {
                sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also {
                    sensorManager.registerListener(
                        this,
                        it,
                        SensorManager.SENSOR_DELAY_FASTEST,
                        SensorManager.SENSOR_DELAY_FASTEST)
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides = event.values[0]
            val upDown = event.values[1]

            binding.tAccel.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED

            binding.tAccel.setBackgroundColor(color)
            binding.tAccel.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"
        } else if (event?.sensor?.type == Sensor.TYPE_PROXIMITY){
            val distance = event.values[0]
            binding.tProx.textSize = distance*10
            binding.tProx.text = "proximidade ${distance}"
        } else if(event?.sensor?.type ==  Sensor.TYPE_LIGHT){
            val lum = ((event.values[0]-0)*(255-0)/(40000-0)).toInt()
            binding.tLuz.setBackgroundColor(Color.argb(255, lum,0, 0))
            binding.tLuz.text = "${lum}"
        } else if(event?.sensor?.type == Sensor.TYPE_GYROSCOPE){
            val sides = event.values[0]
            val upDown = event.values[1]
            val zindex = event.values[2]

            binding.tGiro.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED

            binding.tGiro.setBackgroundColor(color)
            binding.tGiro.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}\nZ: ${zindex}"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}
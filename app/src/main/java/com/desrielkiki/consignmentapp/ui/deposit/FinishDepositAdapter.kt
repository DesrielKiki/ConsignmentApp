import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.desrielkiki.consignmentapp.data.entity.DepositProductData
import com.desrielkiki.consignmentapp.data.entity.relation.SubDepositWithProductData
import com.desrielkiki.consignmentapp.data.helper.DepositProductDiffUtil
import com.desrielkiki.consignmentapp.databinding.RowFinishDepositBinding

class FinishDepositAdapter :
    RecyclerView.Adapter<FinishDepositAdapter.DepositProductViewHolder>() {

    private var productList = mutableListOf<SubDepositWithProductData>()

    private var returnQtyChangeListener: ((Map<Int, Int>) -> Unit)? = null


    private val _onReturnQtyChanged = MutableLiveData<Pair<Int, Int>>()
    val onReturnQtyChanged: LiveData<Pair<Int, Int>> = _onReturnQtyChanged

    class DepositProductViewHolder(val binding: RowFinishDepositBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var textWatcher: TextWatcher? = null // Declare as nullable and initialize as null

        fun bind(subDepositWithProductData: SubDepositWithProductData) {
            binding.subDepositWithProductData = subDepositWithProductData
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowFinishDepositBinding.inflate(layoutInflater, parent, false)

        return DepositProductViewHolder(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: DepositProductViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.bind(currentItem)

        // Set a TextWatcher to update the returnQty value in the productList
        holder.binding.etReturnQty.removeTextChangedListener(holder.textWatcher) // Remove previous TextWatcher to avoid conflicts
        holder.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = s.toString()
                val returnQty: Int = if (inputText.isNotBlank()) {
                    inputText.toInt()
                } else {
                    0
                }

                val currentItem = productList[holder.adapterPosition]

                val validatedReturnQty = returnQty.coerceAtMost(currentItem.depositProductData.qty)

                currentItem.depositProductData.returnQty = validatedReturnQty

                if (inputText != validatedReturnQty.toString()) {
                    holder.binding.etReturnQty.setText(validatedReturnQty.toString())
                    holder.binding.etReturnQty.setSelection(validatedReturnQty.toString().length)
                }

                _onReturnQtyChanged.value = Pair(holder.adapterPosition, validatedReturnQty)

                val updatedReturnQtyMap = mapOf(holder.adapterPosition to validatedReturnQty)
                returnQtyChangeListener?.let {
                    it(updatedReturnQtyMap)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        holder.binding.etReturnQty.addTextChangedListener(holder.textWatcher)
        setReturnQtyInputFilter(holder.binding.etReturnQty, currentItem.depositProductData.qty)
    }

    fun setProductData(subDepositWithProductData: List<SubDepositWithProductData>) {
        val productDiffUtil = DepositProductDiffUtil(productList, subDepositWithProductData)
        val productDiffUtilResult = DiffUtil.calculateDiff(productDiffUtil)
        this.productList.clear()
        this.productList.addAll(subDepositWithProductData)
        productDiffUtilResult.dispatchUpdatesTo(this)
    }

    fun getProductList(): List<SubDepositWithProductData> {
        return productList
    }

    private fun setReturnQtyInputFilter(editText: EditText, qty: Int) {
        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            try {
                val input = source.toString().toInt()
                if (input <= qty) {
                    null // Accept the input as it is within the range
                } else {
                    "" // Return an empty string to block the input
                }
            } catch (e: NumberFormatException) {
                "" // Return an empty string to block the input if it's not a valid number
            }
        }
        editText.filters = arrayOf(inputFilter)
    }
}
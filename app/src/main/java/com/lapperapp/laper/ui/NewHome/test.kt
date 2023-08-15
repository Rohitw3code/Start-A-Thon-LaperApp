//package com.lapperapp.laper.ui.NewHome
//
//// Define a RecyclerView adapter with a listener interface
//class MyAdapter(private val data: List<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
//
//    interface OnItemClickListener {
//        fun onItemClick(item: String)
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//        val text: TextView = itemView.findViewById(android.R.id.text1)
//
//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View?) {
//            val position = adapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                val item = data[position]
//                listener.onItemClick(item)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.text.text = data[position]
//    }
//
//    override fun getItemCount() = data.size
//}
//
//// In MainActivity, set up the RecyclerView and button
//class MainActivity : AppCompatActivity(), MyAdapter.OnItemClickListener {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
//        val button: Button = findViewById(R.id.button)
//
//        val data = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
//
//        val adapter = MyAdapter(data, this)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        button.setOnClickListener {
//            // Get the selected item from the adapter and pass it to the activity
//            val selectedItem = adapter.getSelectedItem()
//            Toast.makeText(this, "Selected item: $selectedItem", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Implement the OnItemClickListener interface
//    override fun onItemClick(item: String) {
//        // Save the selected item in the adapter
//        val adapter = recyclerView.adapter as MyAdapter
//        adapter.setSelectedItem(item)
//    }
//}

# scrubdemolooper

See https://github.com/sauravrp/scrubdemo but without using Universal Image Loader. Using Looper, Handlers and Handler Thread. Added a Memory LruCache. Also reusing bitmap on subsequent loading of the images since the image sizes are the same which significanlty reduces GC kickoffs. Using this method is a lot easier and definitely more readable than trying to use AsyncTask.

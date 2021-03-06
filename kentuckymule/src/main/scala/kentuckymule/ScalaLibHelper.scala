package kentuckymule

import java.nio.file.{FileSystems, Files}

import dotty.tools.dotc.core.Contexts.Context
import kentuckymule.core._
import kentuckymule.core.Symbols.{ClassSymbol, ModuleSymbol, PackageSymbol, StubClassSymbol, StubModuleSymbol, Symbol}
import kentuckymule.core.Types.{ClassInfoType, ModuleInfoType}
import kentuckymule.queue.JobQueue

object ScalaLibHelper {
  import dotty.tools.dotc.core.Decorators._
  import dotty.tools.dotc.core.NameOps._
  def enterStabSymbolsForScalaLib(implicit jobQueue: JobQueue, enter: Enter, ctx: Context): Unit = {
    // enter java.io
    val root = ctx.definitions.rootPackage
    val javaPkg = enterStubPackage("java", root)
    val ioPkg = enterStubPackage("io", javaPkg)
    enterStubClasses(ioPkg, "ByteArrayOutputStream", "OutputStreamWriter", "PrintStream", "IOException", "Writer",
    "Serializable", "ObjectOutputStream", "ObjectInputStream", "File", "FileReader", "Reader", "PrintWriter",
    "FileInputStream", "PrintStream", "Closeable", "InputStream", "PushbackReader", "BufferedReader",
      "InputStreamReader", "OutputStream", "Flushable", "InterruptedIOException", "FilterInputStream", "FilterOutputStream",
    "PipedInputStream", "PipedOutputStream", "FileOutputStream")

    // enter java.util
    val javaUtilPkg = enterStubPackage("util", javaPkg)
    enterStubClasses(javaUtilPkg, "Collection", "NoSuchElementException", "WeakHashMap", "Arrays",
    "ConcurrentModificationException", "Enumeration", "Dictionary", "Properties", "Comparator", "Iterator", "List",
    "Set", "Map", "AbstractCollection", "AbstractCollection", "AbstractList", "AbstractSet", "AbstractMap", "Properties",
    "Random", "Locale")

    locally {
      val javaUtilMapObj = enterStubObject(javaUtilPkg, "Map")
      enterStubClass(javaUtilMapObj, "Entry")
    }

    // enter java.util.regex
    val javaUtilRegexPkg = enterStubPackage("regex", javaUtilPkg)
    enterStubClasses(javaUtilRegexPkg, "Pattern", "Matcher")

    // enter java.util.concurrent
    val javaUtilConcurrentPkg = enterStubPackage("concurrent", javaUtilPkg)
    enterStubClasses(javaUtilConcurrentPkg, "ForkJoinWorkerThread", "ForkJoinTask", "Callable",
      "Executor", "ExecutorService", "ThreadFactory", "TimeUnit", "CountDownLatch", "LinkedTransferQueue",
      "RecursiveAction", "RecursiveTask", "ThreadLocalRandom", "ExecutionException", "CancellationException",
      "TimeoutException", "ConcurrentMap", "LinkedBlockingQueue")

    // enter java.util.concurrent.ForkJoinPool and .ForkJoinWorkerThreadFactory
    {
      // enter both ForkJoinClass and ForkJoin module (object)
      enterStubClass(javaUtilConcurrentPkg, "ForkJoinPool")
      val forkJoinPoolObj = enterStubObject(javaUtilConcurrentPkg, "ForkJoinPool")
      enterStubClass(forkJoinPoolObj, "ForkJoinWorkerThreadFactory")
      enterStubClass(forkJoinPoolObj, "ManagedBlocker")
    }

    // enter java.util.concurrent.atomic
    val javaUtilConcurrentAtomicPkg = enterStubPackage("atomic", javaUtilConcurrentPkg)
    enterStubClasses(javaUtilConcurrentAtomicPkg, "AtomicInteger", "AtomicReference", "AtomicReferenceFieldUpdater")

    // java.util.concurrent.locks
    val javaUtilConcurrentLocksPkg = enterStubPackage("locks", javaUtilConcurrentPkg)
    enterStubClasses(javaUtilConcurrentLocksPkg, "AbstractQueuedSynchronizer", "ReentrantReadWriteLock")

    // enter java.lang
    val javaLangPkg = enterStubPackage("lang", javaPkg)
    enterStubClasses(javaLangPkg, "String", "CharSequence", "Class", "Throwable", "Runnable", "Double", "Long",
      "Integer", "Exception", "Error", "RuntimeException", "NullPointerException", "ClassCastException",
      "IndexOutOfBoundsException", "ArrayIndexOutOfBoundsException", "StringIndexOutOfBoundsException",
      "UnsupportedOperationException", "IllegalArgumentException", "NumberFormatException", "AbstractMethodError",
      "InterruptedException", "System", "StringBuilder", "StackOverflowError", "Cloneable", "InheritableThreadLocal",
      "Object", "ClassLoader", "Character", "Short", "Boolean", "Float", "Comparable", "Thread", "Byte",
      "Iterable", "Runtime", "Process", "ProcessBuilder", "StringBuffer", "Appendable")
    locally {
      val threadObj = enterStubObject(javaLangPkg, "Thread")
      enterStubClass(threadObj, "UncaughtExceptionHandler")
    }

    // enter java.lang.ref
    val javaLangRefPkg = enterStubPackage("ref", javaLangPkg)
    enterStubClasses(javaLangRefPkg, "PhantomReference", "WeakReference", "SoftReference", "ReferenceQueue",
      "Reference")

    // enter java.lang.reflect
    val javaLangReflectPkg = enterStubPackage("reflect", javaLangPkg)
    enterStubClasses(javaLangReflectPkg, "AccessibleObject", "Method", "Field")

    // enter java.lang.invoke
    val javaLangInvoke = enterStubPackage("invoke", javaLangPkg)
    locally {
      enterStubClasses(javaLangInvoke, "SerializedLambda", "MethodHandle")
      val methodHandlesObj = enterStubObject(javaLangInvoke, "MethodHandles")
      enterStubClass(methodHandlesObj, "Lookup")
    }


    // enter java.beans
    val javaBeansPkg = enterStubPackage("beans", javaPkg)
    enterStubClasses(javaBeansPkg, "Introspector", "SimpleBeanInfo")

    // enter java.text
    val javaTextPkg = enterStubPackage("text", javaPkg)
    enterStubClasses(javaTextPkg, "MessageFormat")

    // enter java.net
    val javaNetPkg = enterStubPackage("net", javaPkg)
    enterStubClasses(javaNetPkg, "URI", "URL")

    // enter java.nio
    val javaNioPkg = enterStubPackage("nio", javaPkg)

    // enter java.nio.charset
    val javaNioCharsetPkg = enterStubPackage("charset", javaNioPkg)
    enterStubClasses(javaNioCharsetPkg, "Charset", "CharsetDecoder", "CharsetEncoder", "CharacterCodingException",
      "CodingErrorAction")

    // enter java.math
    val javaMathPkg = enterStubPackage("math", javaPkg)
    enterStubClasses(javaMathPkg, "MathContext", "BigDecimal", "RoundingMode", "BigInteger")

    // enter scala
    val scalaPkg = enterStubPackage("scala", root)

    enterStubClasses(scalaPkg, "Any", "AnyRef", "Nothing", "Unit", "Null")

    // enter scala.runtime
    val scalaRuntimePkg = enterStubPackage("runtime", scalaPkg)
    enterStubClasses(scalaRuntimePkg, "BoxedUnit")

    // enter scala.math
    val scalaMathPkg = enterStubPackage("math", scalaPkg)
    enterStubClasses(scalaMathPkg, "ScalaNumber")

    // enter scala.collection.concurrent
    val scalaCollectionPkg = enterStubPackage("collection", scalaPkg)
    val scalaConcurrentPkg = enterStubPackage("concurrent", scalaCollectionPkg)
    enterStubClasses(scalaConcurrentPkg, "BasicNode", "INodeBase", "MainNode", "CNodeBase", "Gen")
    enterStubObject(scalaConcurrentPkg, "INodeBase")
  }

  def enterStubPackage(name: String, owner: PackageSymbol)(implicit jobQueue: JobQueue,
                                                           enter: Enter, context: Context): PackageSymbol = {
    val pkgSym = PackageSymbol(name.toTermName)
    val pkgCompleter = new PackageCompleter(pkgSym)
    pkgSym.completer = pkgCompleter
    jobQueue.queueCompleter(pkgCompleter, pushToTheEnd = false)
    owner.addChild(pkgSym)
    pkgSym
  }

  private class StubClassCompleter(sym: ClassSymbol) extends Completer(sym) {
    var cachedInfo: ClassInfoType = _
    override def complete()(implicit context: Context): CompletionResult = {
      if (cachedInfo == null) {
        import kentuckymule.core.Types._
        cachedInfo = new ClassInfoType(sym, Nil, null)
        cachedInfo.members.enterAll(sym.decls)
      }
      CompletedType(cachedInfo)
    }
    override def isCompleted: Boolean = cachedInfo != null
  }

  private class StubModuleCompleter(sym: ModuleSymbol) extends Completer(sym) {
    var cachedInfo: ModuleInfoType = _
    override def complete()(implicit context: Context): CompletionResult = {
      import kentuckymule.core.Types._
      if (cachedInfo == null) {
        val clsInfo = if (sym.clsSym.isComplete)
          sym.clsSym.info
        else
          return IncompleteDependency(sym.clsSym)
        cachedInfo = new ModuleInfoType(sym, clsInfo)
      }
      CompletedType(cachedInfo)
    }
    override def isCompleted: Boolean = cachedInfo != null
  }

  private def enterStubClasses(pkg: PackageSymbol, classNames: String*)
                              (implicit jobQueue: JobQueue, context: Context): Unit = {
    for (className <- classNames) {
      enterStubClass(pkg, className)
    }
  }

  private def enterStubClass(parent: StubModuleSymbol, className: String)
                            (implicit jobQueue: JobQueue, context: Context): StubClassSymbol = {
    enterStubClass(parent.clsSym, className)
  }

  private def enterStubClass(parent: Symbol, className: String)
                            (implicit jobQueue: JobQueue, context: Context): StubClassSymbol = {
    val cls = new StubClassSymbol(className.toTypeName, parent)
    cls.completer = new StubClassCompleter(cls)
    jobQueue.queueCompleter(cls.completer)
    parent.addChild(cls)
    cls
  }

  private def enterStubObject(parent: Symbol, objectName: String)
                             (implicit jobQueue: JobQueue, context: Context): StubModuleSymbol = {
    val modClsSym = new StubClassSymbol(objectName.toTypeName.moduleClassName, parent)
    modClsSym.completer = new StubClassCompleter(modClsSym)
    val modSym = new StubModuleSymbol(objectName.toTermName, modClsSym, parent)
    modSym.completer = new StubModuleCompleter(modSym)
    jobQueue.queueCompleter(modSym.completer)
    parent.addChild(modSym)
    modSym
  }

  def scalaLibraryFiles(scalaLibDir: String): Array[String] = {
    import java.nio.file.Paths
    val projectDir = Paths.get(scalaLibDir)
    val matcher = FileSystems.getDefault.getPathMatcher("glob:**/*.scala")
    val fileStream = Files.find(projectDir, 255, (path, attrs) => matcher.matches(path))
    import scala.collection.JavaConverters._
    fileStream.map[String](_.toAbsolutePath.toString).iterator().asScala.toArray
  }
}
